package com.example.tjliqy.smsgrouphelper.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tjliqy.smsgrouphelper.R;
import com.example.tjliqy.smsgrouphelper.bean.AddList;
import com.example.tjliqy.smsgrouphelper.bean.Address;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjliqy on 2016/9/2.
 */
public class RvSendAdapter extends RecyclerView.Adapter<RvSendAdapter.RvViewHolder>{

    private Context context;

//    private List<Address> beanList = new ArrayList<>();

    public RvSendAdapter(Context context) {
        this.context = context;
    }

//    public void changeStatus(int position){
//        beanList.get(position).setSend(true);
//        notifyItemChanged(position);
//    }
//
//    public void add(List<Address> beanList){
//        this.beanList.addAll(beanList);
//        notifyDataSetChanged();
//    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        holder.tvName.setText(position + ". " + AddList.getInstance().getList().get(position).getRealname());
        holder.tvNumber.setText(AddList.getInstance().getList().get(position).getPhone());
        if (AddList.getInstance().getList().get(position).getStatus() == 1){
            holder.tvSend.setText("已发送");
            holder.tvSend.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
        }
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RvViewHolder viewHolder = new RvViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add,parent,false));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return AddList.getInstance().size();
    }

    class RvViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_send)
        TextView tvSend;

        public RvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
