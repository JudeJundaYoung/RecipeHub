package edu.neu.recipehub.utils;

import android.provider.ContactsContract;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.neu.recipehub.fragments.adapters.RecipeItemAdapter;
import edu.neu.recipehub.objects.AlgoliaKey;
import edu.neu.recipehub.objects.Recipe;
import edu.neu.recipehub.objects.RecipeEntry;

public class SearchResultFinder {
    private DatabaseReference mDatabaseRef;
    private Index index;
    private List<String> keyList;
    private RecipeItemAdapter recipeItemAdapter;
    private List<Map.Entry<String, Recipe>> searchResult;

    public SearchResultFinder(RecipeItemAdapter recipeItemAdapter, List<Map.Entry<String, Recipe>> searchResult) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub").child("recipe");
        Client client = new Client(AlgoliaKey.ALGOLIA_ID, AlgoliaKey.ALGOLIA_ADMIN_KEY);
        index = client.getIndex("RecipeHub");
        keyList = new ArrayList<>();
        this.recipeItemAdapter = recipeItemAdapter;
        this.searchResult = searchResult;
    }


    public void search(String input) {
        CompletionHandler completionHandler = new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                keyList.clear();
                searchResult.clear();
                recipeItemAdapter.notifyDataSetChanged();
                searchResultJsonParser(jsonObject);
                for (String key : keyList) {
                    DatabaseReference ref = mDatabaseRef.child(key);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String recipeKey = dataSnapshot.getKey();
                            Recipe recipe = dataSnapshot.getValue(Recipe.class);
                            searchResult.add(new RecipeEntry<String, Recipe>(recipeKey, recipe));
                            recipeItemAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
        Query query = new Query();
        query.setQuery(input);
        // query.setHitsPerPage(20);
        index.searchAsync(query, completionHandler);
    }

    private void searchResultJsonParser(JSONObject searchResult) {
        if (searchResult == null) return;
        JSONArray hits = searchResult.optJSONArray("hits");
        if (hits == null) return;
        for (int i = 0; i < hits.length(); i++) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit == null) continue;
            keyList.add(hit.optString("objectID"));
        }
    }

//    public static void main(String[] args) {
////        class test {
////            public void func( final int num) {
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        try{
////                            synchronized (this) {
////                                wait(10000/num);
////                            }
////                            System.out.println(num);
////                        } catch (Exception ex) {
////                            ex.printStackTrace();
////                        }
////
////                    }
////                }).start();
////                return;
////            }
////        }
//        final int[] ca = {5};
//        for(int i = 1; i<=2; i++) {
//            ca[0] = i;
//            final long time = 10000/i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        synchronized (this) {
//                            wait(time);
//                        }
//                        System.out.println(ca[0]);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//
//                }
//            }).start();
//        }
//
////        String s = "a";
////        String r = "a";
////        System.out.println(s==r);
////        for(int i=0; i<2; i++)
    //  }


}
