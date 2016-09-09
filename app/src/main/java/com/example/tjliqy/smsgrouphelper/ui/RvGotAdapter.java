package com.example.tjliqy.smsgrouphelper.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tjliqy.smsgrouphelper.R;
import com.example.tjliqy.smsgrouphelper.bean.AddList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjliqy on 2016/9/8.
 */
public class RvGotAdapter extends RecyclerView.Adapter<RvGotAdapter.RvViewHolder>{

    private Context context;


    public RvGotAdapter(Context context) {
        this.context = context;
    }

//    public void changeStatus(int position){
//        beanList.get(position).setSend(true);
//        notifyItemChanged(position);
//    }
//
//    public void add(List<Address> beanList){
//        this.beanList.clear();
//        this.beanList.addAll(beanList);
//        notifyDataSetChanged();
//    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        holder.tvName.setText(position + ". " + AddList.getInstance().getList().get(position).getRealname());
        holder.tvNumber.setText(AddList.getInstance().getList().get(position).getPhone());
        holder.tvDetail.setText(AddList.getInstance().getList().get(position).getDetail());
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RvViewHolder viewHolder = new RvViewHolder(LayoutInflater.from(context).inflate(R.layout.item_got,parent,false));
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
        @BindView(R.id.tv_detail)
        TextView tvDetail;

        public RvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
