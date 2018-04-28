package com.example.mathi.smartexpense.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathi.smartexpense.R;
import com.example.mathi.smartexpense.network.HttpGetRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

        //getItem(position) va récupérer l'item [position] de la List<ExpenseReport> expenseReports
        final ExpenseReport er = getItem(position);

        //comme nos vues sont réutilisées, notre cellule possède déjà un ViewHolder
        ListViewExpenseReport viewHolder = (ListViewExpenseReport) convertView.getTag();
        if(viewHolder == null){
            //si elle n'avait pas encore de ViewHolder
            viewHolder = new ListViewExpenseReport();
            viewHolder.date = (TextView) convertView.findViewById(R.id.dateExpenseReport);
            viewHolder.city = (TextView) convertView.findViewById(R.id.cityExpenseReport);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.commentExpenseReport);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.expenseTotal);
            viewHolder.deleteER = (Button) convertView.findViewById(R.id.deleteButtonER);
            if (!er.getSubmissionDate().equals("null")) {
                viewHolder.deleteER.setVisibility(View.GONE);
            } else {
                viewHolder.deleteER.setVisibility(View.VISIBLE);
                final ListViewExpenseReport finalViewHolder = viewHolder;
                final View finalConvertView = convertView;
                viewHolder.deleteER.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                        builder1.setMessage("Voulez-vous supprimer la note de frais " + er.getCity() + " ?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Confirmer",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        //String myURL = "http://www.gyejacquot-pierre.fr/API/public/delete/expense?idExpense="+String.valueOf(e.getIdExpense())+"&category="+String.valueOf(e.getLabel());
                                        String myURL = "http://10.0.2.2/smartExpenseApi/API/public/delete/er?expenseReportCode=" + String.valueOf(er.getCode());

                                        HttpGetRequest getRequest = new HttpGetRequest();
                                        String result = "";
                                        try {
                                            result = getRequest.execute(myURL).get();
                                            System.out.println("Retour HTTPGetRequest : " + result);
                                        } catch (InterruptedException | ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                        if (result.equals("Succes")) {
                                            remove(er);
                                            Toast.makeText(getContext(), "Note de frais supprimée", Toast.LENGTH_SHORT).show();
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
