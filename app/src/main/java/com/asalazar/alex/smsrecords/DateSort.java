package com.asalazar.alex.smsrecords;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjand on 2/18/2016.
 */
public class DateSort {

    private String[] array;
    private String[] tempMergArr;
    private int length;
    private int index;

    public DateSort(List<String> l, int i){
        //printInts(l);
        index = i;
        sort(ListToArray(l));
    }

    public void printInts(List<String> l){

        for(int i = 0; i < l.size(); ++i){
            String[] q = l.get(i).split(",");
            String w = q[1];
            long temp = Long.valueOf(w).longValue();
            System.out.println(temp);
            //System.out.println(l.get(i).split(",")[1].getClass().getName());
        }
    }

    public String[] ListToArray(List<String> l){
        String[] s = new String[l.size()];

        for(int i = 0; i < s.length; ++i){
            s[i] = l.get(i);
        }
        return s;
    }

    public List<String> ArrayToString(String[] arr){
        List<String> l = new ArrayList<>();
        for(int i = 0; i < arr.length; ++i){
            l.add(arr[i]);
        }
        return l;
    }

    public List<String> getSortedDates(){
        List<String> returnList = ArrayToString(this.array);
        return returnList;
    }

    public void sort(String inputArr[]) {
        this.array = inputArr;
        this.length = inputArr.length;
        this.tempMergArr = new String[length];
        doMergeSort(0, length - 1);
    }

    private void doMergeSort(int lowerIndex, int higherIndex) {

        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            doMergeSort(lowerIndex, middle);
            // Below step sorts the right side of the array
            doMergeSort(middle + 1, higherIndex);
            // Now merge both sides
            mergeParts(lowerIndex, middle, higherIndex);
        }
    }

    private void mergeParts(int lowerIndex, int middle, int higherIndex) {

        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = array[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {

            if (Long.valueOf(tempMergArr[i].split(",")[index]).longValue() >= Long.valueOf(tempMergArr[j].split(",")[index]).longValue()) {
                array[k] = tempMergArr[i];
                i++;
            } else {
                array[k] = tempMergArr[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = tempMergArr[i];
            k++;
            i++;
        }

    }
}