// OrderHistoryAdapter.java
package com.example.ujicoba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pesanan, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderNumber.setText(order.getOrderNumber());
        holder.tvOrderSummary.setText(order.getProductSummary());
        holder.tvOrderTimestamp.setText(order.getOrderTimestamp());
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvOrderTotal.setText(formatRupiah(order.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String formatRupiah(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvOrderSummary, tvOrderTimestamp, tvOrderStatus, tvOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            tvOrderSummary = itemView.findViewById(R.id.tv_order_summary);
            tvOrderTimestamp = itemView.findViewById(R.id.tv_order_timestamp);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
        }
    }
}