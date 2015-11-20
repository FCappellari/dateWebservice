package utils;

import java.util.Comparator;

import model.Photo;

public class CustomComparator implements Comparator<Photo> {
    @Override
    public int compare(Photo o1, Photo o2) {    	
        return Integer.compare(o2.getLikes(), o1.getLikes());
    }
}