package com.dgu.du_smart_bus;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class App {
    private Firestore db; 
    private final static String COLLECTION_NAME = "du_smart_bus";   //컬렉션 이름
    
    
    public static void main( String[] args ) {
        App app = new App();
        try {
            app.init();
            app.makeDatabaseConn();
            app.select();
            //app.insert();
            //app.update();
            //app.delete();            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void init() throws Exception{
        FileInputStream refreshToken = new FileInputStream("dusmartbusfirebase.json");//json파일 경로

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(refreshToken))
            .setDatabaseUrl("https:/du-smart-bus.firebaseio.com/") //db url
            .build();
        
        FirebaseApp.initializeApp(options);
    }
    
    private void makeDatabaseConn(){  //Firestore 인스턴스 생성
        db = FirestoreClient.getFirestore();
    }    
    
    private void select() throws Exception{ //조회
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME).get();
    
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("name: " + document.getString("name"));
            System.out.println("id: " + document.get("numbers"));
            System.out.println("id: " + document.getString("id"));
            System.out.println("booleans: " + document.getBoolean("booleans"));
        }
    }    

    private void insert(){  //등록
        DocumentReference docRef = db.collection(COLLECTION_NAME).document("test6");
        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("name", "kim");
        data.put("numbers", "1234");
        data.put("id", "kk");

    try {
        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());
    } catch (InterruptedException e) {
        // InterruptedException 처리
        System.err.println("Thread was interrupted while waiting for the result.");
    } catch (ExecutionException e) {
        // ExecutionException 처리
        System.err.println("An error occurred while executing the Firestore operation: " + e.getCause());
    }
    }

    private void update(){  //수정
        Map<String, Object> update = new HashMap<String, Object>();
        update.put("name", "HELLO-WORLD9");
        update.put("numbers", 2341);
        update.put("booleans", false);    	
        db.collection(COLLECTION_NAME).document("test4").update(update);
    }

    private void delete(){  //삭제
        db.collection(COLLECTION_NAME).document("test1").delete();
    }  
}