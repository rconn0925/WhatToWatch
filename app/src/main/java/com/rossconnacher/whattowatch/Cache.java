package com.rossconnacher.whattowatch;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Ross on 5/28/2017.
 */

public final class Cache {
    private Cache() {}

    public static void writeObject(Context context, String fileName, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.flush();
        oos.close();

        fos.close();
    }

    public static Object readObject(Context context, String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        fis.close();
        return object;
    }
}