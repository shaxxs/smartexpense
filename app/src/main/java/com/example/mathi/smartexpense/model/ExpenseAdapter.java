package com.example.mathi.smartexpense.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.smartexpense.DashboardActivity;
import com.example.mathi.smartexpense.R;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mathi on 13/04/2018.
 */

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    //expenses est la liste des models à afficher
    public ExpenseAdapter(Context context, List<Expense> expenses) {
        super(context, 0, expenses);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_expense,parent, false);
        }

        //getItem(position) va récupérer l'item [position] de la List<ExpenseReport> expenseReports
        final Expense e = getItem(position);

        //comme nos vues sont réutilisées, notre cellule possède déjà un ViewHolder
        ListViewExpense viewHolder = (ListViewExpense) convertView.getTag();
        if(viewHolder == null){
            //si elle n'avait pas encore de ViewHolder
            viewHolder = new ListViewExpense();
            viewHolder.dateExpense = (TextView) convertView.findViewById(R.id.dateExpense);
            viewHolder.categoryExpense = (TextView) convertView.findViewById(R.id.labelExpense);
            viewHolder.commentExpense = (TextView) convertView.findViewById(R.id.commentExpense);
            viewHolder.amountExpense = (TextView) convertView.findViewById(R.id.amountExpense);
            viewHolder.deleteE = (Button) convertView.findViewById(R.id.deleteButtonE);
            if (!e.getSubmissionDate().equals("null")) {
                viewHolder.deleteE.setVisibility(View.GONE);
            } else {
                viewHolder.deleteE.setVisibility(View.VISIBLE);
                final ListViewExpense finalViewHolder = viewHolder;
                final View finalConvertView = convertView;
                viewHolder.deleteE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                        builder1.setMessage("Voulez-vous supprimer la dépense " + e.getLabel() + " ?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Confirmer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        //String myURL = "http://www.gyejacquot-pierre.fr/API/public/delete/expense?idExpense="+String.valueOf(e.getIdExpense())+"&category="+String.valueOf(e.getLabel());
                                        String myURL = "http://10.0.2.2/smartExpenseApi/API/public/delete/expense?idExpense=" + String.valueOf(e.getIdExpense()) + "&category=" + String.valueOf(e.getLabel());

                                        HttpGetRequest getRequest = new HttpGetRequest();
                                        String result = "";
                                        try {
                                            result = getRequest.execute(myURL).get();
                                            System.out.println("Retour HTTPGetRequest : " + result);
                                        } catch (InterruptedException | ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                        if (result.equals("Succes")) {
                                            remove(e);
                                            Toast.makeText(getContext(), "Dépense supprimée", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        builder1.setNegativeButton(
                                "Annuler",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                });
            }

            convertView.setTag(viewHolder);
        }

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