package com.otec.demo.util;

import com.otec.demo.exceptions.ObjectNullException;

public class Util {

    /**
     * Function that checks if an Object is null.
     * @param o Object
     * @throws ObjectNullException if it's null.
     */
    public static void checkObject(Object o){
        if(o==null)
            throw new ObjectNullException("Objet "+o.getClass().getName()+" is null");
    }


}
