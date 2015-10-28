//package edu.rit.gis.doctoreducator.MedicalInfo;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.SectionIndexer;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Set;
//
//import edu.rit.gis.doctoreducator.R;
//
///**
// * Created by siddeshpillai on 10/23/15.
// */
//public class MedicinesListActivity extends Activity {
//
//    private LinkedList<Medicines> storeList = new LinkedList<Medicines>();
//    private StoreListAdaptor storeListAdaptor;
//
//    private ListView list;
//
//    /**
//     * Called when the activity is first created.
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.drug_responses);
//
//        list = (ListView) findViewById(R.id.thelist);
//
////        // populate the store list
////        storeList = db.getAllStoresOrderByName(storeList);
//
//        storeList.add(new Medicines(1,"Medicine1", "Fever"));
//        storeList.add(new Medicines(2,"Medicine2", "Cough"));
//        storeList.add(new Medicines(3,"Medicine3", "Cold"));
//
//
//        // create an adaptor with the store list
//        storeListAdaptor = new StoreListAdaptor(this, storeList);
//
//        // assign the listview an adaptor
//        list.setAdapter(storeListAdaptor);
//
//    }
//
//    /**
//     * The List row creator
//     */
//    class StoreListAdaptor extends ArrayAdapter<Medicines> implements SectionIndexer{
//
//        HashMap<String, Integer> alphaIndexer;
//        String[] sections;
//
//        public StoreListAdaptor(Context context, LinkedList<Medicines> items) {
//            super(context, R.layout.storerow, items);
//
//            alphaIndexer = new HashMap<String, Integer>();
//            int size = items.size();
//
//            for (int x = 0; x < size; x++) {
//                Medicines s = items.get(x);
//
//                // get the first letter of the store
//                String ch =  s.name.substring(0, 1);
//                // convert to uppercase otherwise lowercase a -z will be sorted after upper A-Z
//                ch = ch.toUpperCase();
//
//                // HashMap will prevent duplicates
//                alphaIndexer.put(ch, x);
//            }
//
//            Set<String> sectionLetters = alphaIndexer.keySet();
//
//            // create a list from the set to sort
//            ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
//
//            Collections.sort(sectionList);
//
//            sections = new String[sectionList.size()];
//
//            sectionList.toArray(sections);
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // expand your row xml if you are using custom xml per row
//        }
//
//        public int getPositionForSection(int section) {
//            return alphaIndexer.get(sections[section]);
//        }
//
//        public int getSectionForPosition(int position) {
//            return 1;
//        }
//
//        public Object[] getSections() {
//            return sections;
//        }
//    }
//}
