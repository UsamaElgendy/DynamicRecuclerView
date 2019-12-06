package com.usama.dynamicrecuclerview.dynamicrecyclerview;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.usama.dynamicrecuclerview.R;
import com.usama.dynamicrecuclerview.utils.MyDataBase;
import com.usama.dynamicrecuclerview.utils.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ItemTouchHelper androidItemTouchHelper ;
    private List<Product> mList;
    private final Activity mActivity;

    public MyAdapter(Activity mActivity ,ItemTouchHelper androidItemTouchHelper) {
        this.androidItemTouchHelper  = androidItemTouchHelper ;

        this.mActivity = mActivity;
        mList = new ArrayList<>();
        // I call update to load the adapter
        update();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = View.inflate(mActivity, R.layout.rv_view, null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MyViewHolder) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;

            Product mProduto = mList.get(i);

            holder.tvName.setText(mProduto.getName());
            holder.tvDrag.setText(mProduto.getValue());
            holder.ivColor.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (androidItemTouchHelper!= null )androidItemTouchHelper.startDrag(holder);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList = MyDataBase.getInstance(mActivity).getProducts();
                Collections.sort(mList,new MyComparator().orderPorPosition);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public void onItemMove(final int initialPosition, final int finalPosition) {
        if (initialPosition < mList.size() && finalPosition < mList.size()) {
            if (initialPosition < finalPosition) {
                for (int i = initialPosition; i < finalPosition; i++) {
                    Collections.swap(mList, i, i + 1);
                }
            } else {
                for (int i = initialPosition; i > finalPosition; i--) {
                    Collections.swap(mList, i, i - 1);
                }
            }

            notifyItemMoved(initialPosition,finalPosition);

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Product mpProduct1 = mList.get(finalPosition);
                Product mpProduct2 = mList.get(initialPosition);

                mpProduct1.setPosition(finalPosition);
                mpProduct2.setPosition(initialPosition);

                MyDataBase.getInstance(mActivity).updateProduct(mpProduct1);
                MyDataBase.getInstance(mActivity).updateProduct(mpProduct2);
            }
        }).start();
    }

    public void removeItem(final int position) {

        Snackbar.make(mActivity.findViewById(R.id.constraint), "are you sure you want delete it ", Snackbar.LENGTH_INDEFINITE)
                .setAction("No", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // remove a product
                        MyDataBase.getInstance(mActivity).removeProduct(mList.get(position));
                        mList.remove(position);
                        notifyItemChanged(position);
                    }
                }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
            }
        }).show();

    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvDrag, ivColor;

        MyViewHolder(View itemView) {
            super(itemView);
            tvName =  itemView.findViewById(R.id.tv_name);
            tvDrag =  itemView.findViewById(R.id.tv_value);
            ivColor =  itemView.findViewById(R.id.iv);

        }


    }


}