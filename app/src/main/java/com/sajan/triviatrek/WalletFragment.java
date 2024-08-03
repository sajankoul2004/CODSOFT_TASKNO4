package com.sajan.triviatrek;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sajan.triviatrek.databinding.FragmentWalletBinding;

public class WalletFragment extends Fragment {



    public WalletFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWalletBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user= documentSnapshot.toObject(User.class);
                        binding.currentCoins.setText(String.valueOf(user.getCoins()));

                    }
                });

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getCoins() >=50000){
                    String uid= FirebaseAuth.getInstance().getUid();
                    String payPal = binding.emailBox.getText().toString();




                    withdrawRequest request=new withdrawRequest(uid,payPal,user.getName());
                    database
                            .collection("withdraws")
                            .document(uid)
                            .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(),"Request Sent Successfully",Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(getContext(),"You Need More Coins To Place Withdraw Request....",Toast.LENGTH_SHORT).show();
                }

            }
        });








        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}