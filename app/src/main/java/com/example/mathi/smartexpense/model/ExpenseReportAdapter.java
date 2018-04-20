package com.example.mathi.smartexpense.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mathi.smartexpense.R;

import java.util.List;

/**
 * Created by mathi on 12/04/2018.
 */

public class ExpenseReportAdapter extends ArrayAdapter<ExpenseReport> {

    //expenseReports est la liste des models à afficher
    public ExpenseReportAdapter(Context context, List<ExpenseReport> expenseReports) {
        super(context, 0, expenseReports);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_expense_report,parent, false);
        }
        //comme nos vues sont réutilisées, notre cellule possède déjà un ViewHolder
        ListViewExpenseReport viewHolder = (ListViewExpenseReport) convertView.getTag();
        if(viewHolder == null){
            //si elle n'avait pas encore de ViewHolder
            viewHolder = new ListViewExpenseReport();
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateExpenseReport);
            viewHolder.city = (TextView) convertView.findViewById(R.id.cityExpenseReport);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.commentExpenseReport);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.expenseTotal);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<ExpenseReport> expenseReports
        ExpenseReport er = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.date.setText(er.getDate());
        viewHolder.city.setText(er.getCity());
        viewHolder.comment.setText(er.getComment());
        viewHolder.amount.setText(String.valueOf(er.getAmount())+"€");
        if (viewHolder.comment.getText().equals("")) {
            viewHolder.comment.setVisibility(View.GONE);
        }

        return convertView;
    }
}
