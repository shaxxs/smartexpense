package com.example.mathi.smartexpense.model;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mathi.smartexpense.R;

import java.util.List;

/**
 * Created by mathi on 13/04/2018.
 */

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    //expenses est la liste des models à afficher
    public ExpenseAdapter(Context context, List<Expense> expenses) {
        super(context, 0, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_expense,parent, false);
        }
        //comme nos vues sont réutilisées, notre cellule possède déjà un ViewHolder
        ListViewExpense viewHolder = (ListViewExpense) convertView.getTag();
        if(viewHolder == null){
            //si elle n'avait pas encore de ViewHolder
            viewHolder = new ListViewExpense();
            viewHolder.dateExpense = (TextView) convertView.findViewById(R.id.dateExpense);
            viewHolder.categoryExpense = (TextView) convertView.findViewById(R.id.labelExpense);
            viewHolder.commentExpense = (TextView) convertView.findViewById(R.id.commentExpense);
            viewHolder.amountExpense = (TextView) convertView.findViewById(R.id.amountExpense);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<ExpenseReport> expenseReports
        Expense e = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.dateExpense.setText(e.getDate());
        viewHolder.categoryExpense.setText(e.getLabel());
        viewHolder.commentExpense.setText(e.getDetails());
        viewHolder.amountExpense.setText(String.valueOf(e.getExpenseTotal())+"€");
        if (viewHolder.commentExpense.getText().equals("")) {
            viewHolder.commentExpense.setVisibility(View.GONE);
        }

        return convertView;
    }
}