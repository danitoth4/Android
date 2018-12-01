package hu.bme.aut.financetrakcer.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;
import hu.bme.aut.financetrakcer.R;
import hu.bme.aut.financetrakcer.model.Finance;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.FinanceViewHolder> {
    private final List<Finance> items;

    private FinanceItemClickListener listener;

    public FinanceAdapter(FinanceItemClickListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public FinanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_finance_list, parent, false);
        return new FinanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceViewHolder holder, int position) {
       Finance item = items.get(position);
       holder.nameTextView.setText(item.name);
       holder.descriptionTextView.setText(item.description);
       holder.amountTextView.setText(item.amount + "Ft");
       holder.firstDateTextView.setText(item.year + "." + item.month + "." + item.day + ".");
       holder.frequencyTextView.setText(item.frequency);
       holder.item = item;
       holder.incomeImageView.setImageResource(getIsIncomeImageResource(item.income));
       holder.categoryTextView.setText(item.category);
       holder.iconImageView.setImageResource(getIconImageResource(item.category));
    }

    private @DrawableRes
    int getIconImageResource(String category) {
        switch(category.toLowerCase()) {
            case "food" :
                return R.drawable.food;
            case "housing" :
                return R.drawable.housing;
            case "bill" :
                return R.drawable.bill;
            case "entertainment" :
                return R.drawable.entertainment;
            case "consumer debt" :
                return R.drawable.debt;
            case "personal care" :
                return R.drawable.personalcare;
            case "health care" :
                return R.drawable.healthcare;
            case "wage" :
                return R.drawable.wage;
            case "saving" :
                return R.drawable.saving;
            default:
                return R.drawable.category;
        }
    }

    private @DrawableRes
    int getIsIncomeImageResource(boolean income) {
        if(income)
            return R.drawable.profit;
        else
            return R.drawable.loss;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Finance item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void deleteItem(Finance item)
    {
        notifyItemRemoved(items.lastIndexOf(item));
        items.remove(item);
    }

    public void update(List<Finance> finances) {
        items.clear();
        items.addAll(finances);
        notifyDataSetChanged();
    }

    public interface FinanceItemClickListener{
        void onItemChanged(Finance item);

        void onItemRemoved(Finance item);
    }

    class FinanceViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView categoryTextView;
        TextView amountTextView;
        TextView firstDateTextView;
        TextView frequencyTextView;
        ImageView incomeImageView;
        ImageButton removeButton;

        Finance item;

        FinanceViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.FinanceIconImageView);
            nameTextView = itemView.findViewById(R.id.FinanceNameTextView);
            descriptionTextView = itemView.findViewById(R.id.FinanceDescriptionTextView);
            categoryTextView = itemView.findViewById(R.id.FinanceCategoryTextView);
            amountTextView = itemView.findViewById(R.id.FinanceAmountTextView);
            firstDateTextView = itemView.findViewById(R.id.firstDateTextView);
            frequencyTextView = itemView.findViewById(R.id.frequencyTextView);
            incomeImageView = itemView.findViewById(R.id.incomeImageView);
            removeButton = itemView.findViewById(R.id.FinanceRemoveButton);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item!=null){
                        listener.onItemRemoved(item);
                        notifyItemRemoved(getAdapterPosition());
                }
            }});
        }
    }
}
