package com.dgu.du_smart_bus;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
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
            app.insert();
            //app.update();
            //app.delete();            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void init() throws Exception{
        FileInputStream refreshToken = new FileInputStream("d:/dvtools/du_smart_bus/dusmartbusfirebase.json");//json파일 경로

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(refreshToken))
            .setDatabaseUrl("https:/du-smart-bus.firebaseio.com/") //db url
            .build();
        
        FirebaseApp.initializeApp(options);
    }
    
    private void makeDatabaseConn(){  //Firestore 인스턴스 생성
        db = FirestoreClient.getFirestore();
    }    
    
    private void select(){ //조회
        db.collection(COLLECTION_NAME).addSnapshotListener( (target, exception)->{
            System.out.println(" - select start - ");
            target.forEach( item->{
                System.out.println("primary id : "+item.getId() + "  ||  value : " + item.getData());
            });
            System.out.println(" - select end - ");
        });
    }    

    private void insert(){  //등록
        Map<Object, Object> item = new HashMap<Object, Object>();
        item.put("name", "HELLO-WORLD5");
        item.put("numbers", 5674);
        item.put("booleans", false);
        db.collection(COLLECTION_NAME).add(item);
    }

    /*private void update(){  //수정
        Map<String, Object> update = new HashMap<String, Object>();
        update.put("name", "HELLO-WORLD9");
        update.put("numbers", 2341);
        update.put("booleans", false);    	
        db.collection(COLLECTION_NAME).document("문서 키 값").update(update);
    }*/

    /*private void delete(){  //삭제
        db.collection(COLLECTION_NAME).document("문서 키 값").delete();
    } */   
}