package com.example.dogwalker;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OwnerListAdapter extends RecyclerView.Adapter<OwnerListAdapter.MyViewHolder> {
    DatabaseReference mDatabase;

    double longitudes;
    double latitudes;
    private List<OwnerProfile> ownerList;

    public OwnerListAdapter(List<OwnerProfile> ownerList) {
        this.ownerList = ownerList;
    }

    public interface onItemClickListener {
        void onItemClick(int pos);
    }
    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    private void requestGeocode(String addr) {
        try {
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();
            String query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(addr, "UTF-8");
            URL url = new URL(query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection != null) {
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "cxzobj9huy");
                connection.setRequestProperty("X-NCP-APIGW-API-KEY", "wQF4VmvAGfParPeLkzPmxW0xmJIV06qxB9nd7ENo");
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                int indexFirst;
                int indexLast;

                indexFirst = stringBuilder.indexOf("\"x\":\"");
                indexLast = stringBuilder.indexOf("\",\"y\":");
                String x = stringBuilder.substring(indexFirst + 5, indexLast);

                indexFirst = stringBuilder.indexOf("\"y\":\"");
                indexLast = stringBuilder.indexOf("\",\"distance\":");
                String y = stringBuilder.substring(indexFirst + 5, indexLast);

                Log.d("지오코드 체크",x+" ");

                longitudes= Double.parseDouble(x);
                latitudes = Double.parseDouble(y);
                Log.d("지오코드 체크2",longitudes+" ");
                bufferedReader.close();
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addItem(OwnerProfile ownerProfile) {
        //Toast.makeText(,Toast.LENGTH_SHORT).show();
        mDatabase =FirebaseDatabase.getInstance().getReference("list");
        Thread datathread = new Thread(){
            @Override
            public void run(){
                requestGeocode(ownerProfile.getAddr());
            }
        };
        datathread.start();
        try {
            datathread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        ownerProfile.setLatitude(latitudes);
        ownerProfile.setLongitude(longitudes);
        Log.d("지오코드 체크3",longitudes+" ");
        ownerList.add(ownerProfile);
        mDatabase.child("owner").push().setValue(ownerProfile);
        notifyDataSetChanged();

    }

    //수정
    private void updateItem(Owner owner, int position) {
        OwnerProfile owner1 = ownerList.get(position);
        owner1.setDogName(owner.getName());
        owner1.setDogAge(owner.getDog_age());
        owner1.setBread(owner.getBreed());
        owner1.setWalkTime(owner.getDog_walk());
        owner1.setAddr(owner.getAddr());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ownerlist_profile, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OwnerProfile owner = ownerList.get(position);
        if (owner != null) {
            holder.dogName.setText(owner.getDogName());
            holder.dogAge.setText(owner.getDogAge());
            holder.dogBreed.setText(owner.getBread());
            holder.dogWalk.setText(owner.getWalkTime());
            holder.addr.setText(owner.getAddr());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = view.inflate(view.getContext(), R.layout.owner_list_add, null);
                EditText dog_name = dialogView.findViewById(R.id.dog_name);
                EditText dog_age = dialogView.findViewById(R.id.dog_age);
                EditText dog_breed = dialogView.findViewById(R.id.dog_breed);
                EditText dog_walk = dialogView.findViewById(R.id.dog_walk);
                EditText dog_addr = dialogView.findViewById(R.id.dog_addr);
                dog_name.setText(owner.getDogName());
                dog_age.setText(owner.getDogAge());
                dog_breed.setText(owner.getBread());
                dog_walk.setText(owner.getWalkTime());
                dog_addr.setText(owner.getAddr());

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("반려인 프로필 수정");
                builder.setView(dialogView);

                builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OwnerProfile owner1 = new OwnerProfile(
                                dog_name.getText().toString(),
                                dog_addr.getText().toString(),
                                dog_breed.getText().toString(),
                                dog_age.getText().toString(),
                                dog_walk.getText().toString());
                        //ownerListAdapter.updateItem(owner);

                    }
                });
                builder.setNegativeButton("닫기", null);
                builder.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return ownerList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dogName, dogAge, dogBreed, dogWalk, addr;
        ImageView imgOwner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
          //  readFirebaseValue();
            dogName = itemView.findViewById(R.id.dogName);
            dogAge = itemView.findViewById(R.id.dogAge);
            dogBreed = itemView.findViewById(R.id.dogBreed);
            dogWalk = itemView.findViewById(R.id.dogWalk);
            addr = itemView.findViewById(R.id.addr);
            imgOwner = itemView.findViewById(R.id.imgOwner);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onItemClickListener.onItemClick(position);
                }
            });

        }




/*        void onBind(Owner owner) {
            dogName.setText(owner.getName());
            dogAge.setText(owner.getDog_age());
            dogBreed.setText(owner.getBreed());
            dogWalk.setText(owner.getDog_walk());
            addr.setText(owner.getAddr());
            imgOwner.setImageResource(owner.getImg());
        }*/
    }
}