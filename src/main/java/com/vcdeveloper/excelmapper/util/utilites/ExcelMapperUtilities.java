package com.vcdeveloper.excelmapper.util.utilites;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for different utilities (general helpers for things that are not part of the other utility categories available).
 */
public final class ExcelMapperUtilities {

    private static Logger log = LoggerFactory.getLogger(ExcelMapperUtilities.class);

    public static boolean nullAwareEquals(Object o1, Object o2) {
        return (o1 == null) ? o2 == null : o1.equals(o2);
    }
    
    public static boolean collectionNotNullOrEmpty(Collection<?> c) {
        return c != null && !c.isEmpty();
    }
    
    
    public static  List<String> removeEmpyAndNullStringFromList(List<String> l) {
        List<String> list = new ArrayList<String>();
        if (collectionNotNullOrEmpty(l)) {
           l.forEach(item -> addNonEmptyString(item ,list)); 
        }
        return list;
    }
    
    public static boolean addNonEmptyString(String item, List<String> list) {
       if(!StringUtil.isEmpty(item)){
          return list.add(item);
       } else {
           return false;
       }
      
    }

    /**
     * Deep-copies into object into new instance of same class.
     */
    @SuppressWarnings("unchecked")
    public static <T> T magicClone(T object) {
        byte[] serialize = serialize(object);
        return (T) deserialize(serialize);
    }

    public static byte[] serialize(Object object) throws Error {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream out = null;
        try {
            final int initialSize = 10000;
            baos = new ByteArrayOutputStream(initialSize);
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            out.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Could not serialize, object: " + object.getClass().getName(), e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.warn("Unable to close stream.", e);
            }
        }
        return null;
    }

    public static Object deserialize(byte[] data) {
        Object result = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(data));
            result = in.readObject();

        } catch (IOException e) {
            log.error("Could not deserialize!", e);
        } catch (ClassNotFoundException e) {
            log.error("Class missing to be able to deserialize!", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.warn("Unable to close stream.", e);
            }
        }
        return result;
    }

   

    private ExcelMapperUtilities() {
    }

 
}
