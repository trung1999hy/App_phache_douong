package com.example.recipeinstructions.ui.inapp.inappnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.ProductDetails;
import com.example.recipeinstructions.R;
import com.example.recipeinstructions.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class InAppPurchaseAdapterNews extends RecyclerView.Adapter<InAppPurchaseAdapterNews.ViewHolder> {

    private OnClickListener onClickListener;
    private List<ProductDetails> productDetailsList = new ArrayList<>();
    private Context context;

    public void setData(Context context, List<ProductDetails> productDetailsList) {
        this.productDetailsList = productDetailsList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.
                item_subscription, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.display(productDetailsList.get(position));
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSubName;
        private ConstraintLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubName = itemView.findViewById(R.id.tvSubName);
            item = itemView.findViewById(R.id.item);
        }

        public void display(ProductDetails productDetails) {
            if (productDetails == null) return;
            String price = productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
            tvSubName.setText(setTitleValue(productDetails.getProductId(), price));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickItem(productDetails);
                }
            });
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClickItem(ProductDetails item);
    }

    private String setTitleValue(String productId, String price) {
        switch (productId) {
            case Constants.KEY_USD_NEWS:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/5 usd");
            case Constants.KEY_10_USD_NEWS:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/10 usd");
            case Constants.KEY_20_USD_NEWS:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/20 usd");
            case Constants.KEY_50_USD_NEWS:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/50 usd");
            case Constants.KEY_100_USD_NEWS:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/100 usd");
            case Constants.KEY_150_USD_NEWS:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/150 usd");
            case Constants.KEY_200_USD_NEWS:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/200 usd");
            case Constants.KEY_500_USD_NEWS:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/500 usd");
            default:
                return String.format(context.getResources().getString(R.string.message_purchase_one), "/0 usd");
        }
    }
}
