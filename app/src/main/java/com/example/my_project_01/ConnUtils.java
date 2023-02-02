package com.example.my_project_01;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ConnUtils<T> {
    private ArrayList<T> dataList = new ArrayList<>();

    private DatabaseReference databaseReference;

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public void connFireBase(String data, String clazz, String adapter, RecyclerView recyclerView) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        databaseReference = FirebaseDatabase.getInstance().getReference(data);
        Class<T> tClass = (Class<T>) Class.forName(clazz);
        T getClass = (T) tClass.newInstance();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    T fetchData = (T) ds.getValue(getClass.getClass());
                    dataList.add(fetchData);
                }
                if (adapter != null && recyclerView != null) {
                    try {

                        Class AdapterClazz = Class.forName(adapter);
                        Constructor<T> constructor = AdapterClazz.getConstructor(List.class, String.class);
                        T instance = (T) constructor.newInstance(dataList);

                        recyclerView.setAdapter((RecyclerView.Adapter) instance);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}