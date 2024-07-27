package com.example.mvvm.viewmodel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.mvvm.model.Comment;



import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

public class AppViewModelTest {


    /**
     * Testet den Sortieralgorithmus für Kommentare aus dem AppViewModel
     */
    @Test
    public void testSortAlgorithm(){

        ArrayList<Comment> list = new ArrayList<>();

        //bestimmt wie viele Commentobjekte und somit Timestamps zu sortieren generiert werden
        //Die Anzahl der Elemente sollte mindestens 5 Elemente enthalten, um sinnvoll testen zu können
        int numberOfElements = 20;

         //generiere "zufällige" timestamps
        for(int i = 0; i < numberOfElements; i++){
           long offset = (long)(Timestamp.valueOf("2022-01-01 00:00:00").getTime() * Math.random());
            long end = (long)(Timestamp.valueOf("2022-01-01 00:00:00").getTime() * Math.random());
            long diff = end - offset + 1;

            //initialisiere Comment Objekte mit verschiedenen timestamps
            Comment comment = new Comment(0,"Das ist ein Kommentar", null,
                    null,"de",0,new Timestamp(offset + (long)(Math.random() * diff)).toString(),null,null,0);
            list.add(comment);

        }

        System.out.println("Unsortiere Liste:\n");
        for(int index = 0; index < list.size(); index++){

            System.out.println(list.get(index).getCreatedAtT());
        }


        //sortiere die Liste der Comment Objekte in absteigender Reihenfolge mit dem Algorithmus aus dem AppViewModel
        AppViewModel.sortCommentsBasedOnTimestamp(list);



        System.out.println("\n\n\n\nSortiere Liste:\n");
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i).getCreatedAtT());
        }


        //prüfe, ob richtig sortiert wurde

        assertTrue("An Index 0 steht das \"jüngste\" Datum! Value: ",
                (list.get(0).getCreatedAtT().compareTo(list.get(1).getCreatedAtT())) >= 0);
        assertTrue("An Index 0 steht das \"jüngste\" Datum! Value: ",
                (list.get(1).getCreatedAtT().compareTo(list.get(2).getCreatedAtT())) >= 0);
        assertTrue("An Index 0 steht das \"jüngste\" Datum! Value: ",
                (list.get(3).getCreatedAtT().compareTo(list.get(4).getCreatedAtT())) >= 0);
        assertTrue("An Index 0 steht das \"jüngste\" Datum! Value: ",
                (list.get(list.size()-2).getCreatedAtT().compareTo(list.get(list.size()-1).getCreatedAtT())) >= 0);



        assertFalse("An Index 0 steht das \"jüngste\" Datum! Value: ",
                (list.get(0).getCreatedAtT().compareTo(list.get(1).getCreatedAtT())) < 0);
        assertFalse("An Index 0 steht das \"jüngste\" Datum! Value: ",
                (list.get(list.size()-2).getCreatedAtT().compareTo(list.get(list.size()-1).getCreatedAtT())) < 0);


    }

}
