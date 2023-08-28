import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogwalker.OwnerDetail;
import com.example.dogwalker.OwnerListAdapter;
import com.example.dogwalker.OwnerProfile;
import com.example.dogwalker.R;
import com.example.dogwalker.WalkerProfile;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.MyViewHolder> {
DatabaseReference mDatabase;
Context context;
private List<WalkerProfile> walkerProfileList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.walkerlist_profile, parent, false);
        ApplicationListAdapter.MyViewHolder myViewHolder = new ApplicationListAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WalkerProfile walkerProfile = walkerProfileList.get(position);
        if (walkerProfile != null) {

            holder.walkerName.setText(walkerProfile.getWalkerName());
            holder.walkerAddr.setText(walkerProfile.getWalkerAddr());
            holder.walkerCareer.setText(walkerProfile.getWalkerCareer());
            holder.walkerNurture.setText(walkerProfile.getWalkerNurture());
            holder.walkerTel.setText(walkerProfile.getWalkerTel());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = view.inflate(view.getContext(), R.layout.owner_list_add, null);
                Intent intent = new Intent(view.getContext(), OwnerDetail.class);
               // intent.putExtra("dogUUID",owner.getOwnerUUID());
                context.startActivity(intent);
            /*    EditText dog_name = dialogView.findViewById(R.id.dog_name);
                EditText dog_age = dialogView.findViewById(R.id.dog_age);
                EditText dog_breed = dialogView.findViewById(R.id.dog_breed);
                EditText dog_walk = dialogView.findViewById(R.id.dog_walk);
                EditText dog_addr = dialogView.findViewById(R.id.dog_addr);
                dog_name.setText(owner.getDogName());
                dog_age.setText(owner.getDogAge());
                dog_breed.setText(owner.getBread());
                dog_walk.setText(owner.getWalkTime());
                dog_addr.setText(owner.getAddr());*/

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{
    TextView walkerName,walkerAddr,walkerNurture,walkerCareer,walkerTel;

    ImageView imgOwner;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
    //walkerName = itemView.findViewById(R.id.)

    }
}

}
