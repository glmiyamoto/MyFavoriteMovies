package com.glmiyamoto.myfavoritemovies.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.glmiyamoto.myfavoritemovies.utils.ImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by glmiyamoto on 4/10/17.
 */

public abstract class AbstractFileExplorer {

    private Context mContext;
    private String mDirPath;

    public AbstractFileExplorer(final Context context, final String directory) {
        mContext = context;
        mDirPath = context.getApplicationInfo().dataDir + File.separator + directory;
    }

    protected String createDirectory() {
        final File dir = new File(mDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    protected String getDirectoryPath() {
        final File dir = new File(mDirPath);
        if (dir.exists()) {
            return mDirPath;
        }
        return null;
    }

    public boolean hasFile(final String fileName) {
        return new File(mDirPath + File.separator + fileName).exists();
    }

    protected byte[] readFile(final String fileName) {
        boolean error = false;
        final File file = new File(mDirPath + File.separator + fileName);
        if (file.exists()) {
            final byte fileContent[] = new byte[(int) file.length()];
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file.getAbsolutePath());
                inputStream.read(fileContent);
            } catch (FileNotFoundException e) {
                error = true;
            } catch (IOException e) {
                error = true;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        inputStream = null;
                    } catch (IOException e) {
                    }
                }
            }

            if (error) {
                return null;
            }

            return fileContent;
        }

        return null;
    }

    protected boolean createFile(final String fileName, final byte[] bytes) {
        createDirectory();

        OutputStream destinationFile = null;
        try {

            final File newFile = new File(mDirPath + File.separator + fileName);

            if (!newFile.exists()) {
                newFile.createNewFile();
            }

            destinationFile = new FileOutputStream(newFile);
            destinationFile.write(bytes, 0, bytes.length);
            destinationFile.close();

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    protected boolean deleteFile(final String fileName) {
        final File fileToDelete = new File(mDirPath + File.separator
                + fileName);
        return fileToDelete.delete();
    }

    protected static void recursiveDelete(final String directory) {
        final File file = new File(directory);
        if (file.isDirectory()) {
            for (final String lFile : file.list()) {
                recursiveDelete(directory + File.separator + lFile);
            }
        }

        if (file.exists()) {
            file.delete();
        }
    }

    protected void replaceFile(final String fileName, final byte[] bytes) {
        final File toUpdate = new File(mDirPath + File.separator + fileName);

        if (toUpdate.exists()) {
            toUpdate.delete();
        }

        createFile(fileName, bytes);
    }
}
