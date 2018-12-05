package edu.neu.recipehub.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.neu.recipehub.MainActivity;
import edu.neu.recipehub.fragments.forks.AddPhotoFragment;
import edu.neu.recipehub.fragments.forks.DescriptionFragment;
import edu.neu.recipehub.fragments.home.HomeFragment;
import edu.neu.recipehub.fragments.forks.IngredientFragment;
import edu.neu.recipehub.fragments.forks.InstructionFragment;
import edu.neu.recipehub.fragments.forks.UploadFailureFragment;
import edu.neu.recipehub.fragments.forks.UploadSuccessFragment;
import edu.neu.recipehub.objects.AlgoliaKey;
import edu.neu.recipehub.objects.Recipe;
import edu.neu.recipehub.objects.Review;


public class UploadHelper {
    private ContentResolver cR;
    private List<Uri> photoUri;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private Index index;
    private String key;
    private HomeFragment.OnFragmentInteractionListener mListener;

    public UploadHelper(ContentResolver cR, HomeFragment.OnFragmentInteractionListener mListener) {
        this.cR = cR;
        this.mListener = mListener;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub").child("recipe");
        mStorageRef = FirebaseStorage.getInstance().getReference("RecipeHub").child("photoUri");
        Client client = new Client(AlgoliaKey.ALGOLIA_ID, AlgoliaKey.ALGOLIA_ADMIN_KEY);
        index = client.getIndex("RecipeHub");
        photoUri = new ArrayList<>();
        key = mDatabaseRef.push().getKey();
        for (Integer id : AddPhotoFragment.URI_MAP.keySet()) {
            photoUri.add(AddPhotoFragment.URI_MAP.get(id));
        }
    }

    public void upload(boolean[] success) {
        Recipe recipe = new Recipe(MainActivity.USER_NAME, DescriptionFragment.RECIPE_NAME,
                DescriptionFragment.Description, IngredientFragment.INGREDIENT_MAP,
                InstructionFragment.INSTRUCTION_LIST, InstructionFragment.REVIEW_LIST,
                new ArrayList<String>());
        mDatabaseRef.child(key).setValue(recipe);
        UploadPhotoTask uploadPhotoTask = new UploadPhotoTask();
        uploadPhotoTask.execute(photoUri);
    }

    private void uploadToAlgolia() {
        try {
            JSONObject record = generateJSON();
            record.put("objectID", key);
            index.addObjectAsync(record, key, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public JSONObject generateJSON() {
        JSONObject result = new JSONObject();
        try {
            JSONObject ingredients = new JSONObject();
            for (String ing : IngredientFragment.INGREDIENT_MAP.keySet()) {
                ingredients.put(ing, IngredientFragment.INGREDIENT_MAP.get(ing));
            }

            JSONObject instructions = new JSONObject();
            for (int i = 0; i < InstructionFragment.INSTRUCTION_LIST.size(); i++) {
                instructions.put(i + "", InstructionFragment.INSTRUCTION_LIST.get(i));
            }
            JSONObject reviews = new JSONObject();
            for (int i = 0; i < InstructionFragment.REVIEW_LIST.size(); i++) {
                Review review = InstructionFragment.REVIEW_LIST.get(i);
                JSONObject temp = new JSONObject();
                temp.put("mUser", review.mUser.mUserName);
                temp.put("mContent", review.mContent);
                reviews.put(i + "", temp);
            }

            result.put("userName", MainActivity.USER_NAME)
                    .put("recipeName", DescriptionFragment.RECIPE_NAME)
                    .put("description", DescriptionFragment.Description)
                    .put("ingredients", ingredients)
                    .put("instructions", instructions)
                    .put("reviews", reviews);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }


    private class UploadPhotoTask extends AsyncTask<List<Uri>, Void, Boolean[]> {

        @Override
        protected Boolean[] doInBackground(List<Uri>... lists) {
            final Boolean[] success = {true};
            for (int i = 0; i < photoUri.size(); i++) {
                final int pos = i;
                Uri imageUri = photoUri.get(i);
                String fileName = System.currentTimeMillis() + "."
                        + getFileExtension(imageUri);
                StorageReference fileReference = mStorageRef.child(key).child(fileName);
                fileReference.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mDatabaseRef.child(key).child("uris").child(pos + "").
                                        setValue(taskSnapshot.getDownloadUrl().toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                success[0] = false;
                            }
                        });
                if (isCancelled()) {
                    success[0] = false;
                    break;
                }
            }
            return success;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(Boolean[] success) {
            if (!success[0]) {
                mDatabaseRef.child(key).removeValue();
                mStorageRef.child(key).delete();
                mListener.changeFragmentInHomeFragment(UploadFailureFragment.newInstance());
            } else {
                uploadToAlgolia();
                mListener.changeFragmentInHomeFragment(UploadSuccessFragment.newInstance());
            }
        }

        private String getFileExtension(Uri uri) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        }
    }

}
