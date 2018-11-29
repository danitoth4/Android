package hu.bme.aut.financetrakcer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.*;
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
        // TODO implementation
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Finance item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<Finance> finances) {
        items.clear();
        items.addAll(finances);
        notifyDataSetChanged();
    }

    public interface FinanceItemClickListener{
        void onItemChanged(Finance item);
    }

    class FinanceViewHolder extends RecyclerView.ViewHolder {

        FinanceViewHolder(View itemView) {
            super(itemView);
        }
    }
}
