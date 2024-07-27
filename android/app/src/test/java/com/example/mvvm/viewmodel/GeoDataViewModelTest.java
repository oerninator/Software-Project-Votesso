package com.example.mvvm.viewmodel;

import static org.junit.Assert.*;

import com.example.mvvm.model.MapLocation;
import com.example.mvvm.model.Subproject;

import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;


public class GeoDataViewModelTest {

    @Test
    public void ListeNachGeodatenSortierenTest() {
        GeoDataViewModel geo = GeoDataViewModel.getInstance();
        List<Subproject> list = new ArrayList<>();
        List<Subproject> sortedList = new ArrayList<>();

        Subproject s1 = new Subproject(1, 0, 0, 0, 0, "", "", 54.5961,
                10.660, "", "", null, null, new MapLocation(1, 10.660, 54.5961));
        Subproject s2 = new Subproject(1, 0, 0, 0, 0, "", "", 56.5961,
                10.660, "", "", null, null, new MapLocation(1, 10.660, 56.5961));
        Subproject s3 = new Subproject(1, 0, 0, 0, 0, "", "", 58.5961,
                10.660, "", "", null, null, new MapLocation(1, 10.660, 58.5961));

        list.add(s1);
        list.add(s2);
        list.add(s3);

        sortedList.add(s3);
        sortedList.add(s2);
        sortedList.add(s1);

        assertEquals(sortedList, geo.getSortedList(new GeoPoint(58.5961, 10.660), list));
    }


}