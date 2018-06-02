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

    /** L'adapter prend en entrée la liste des dépenses et le layout et génère pour chaque dépense une cellule formatée */

    // expenseReports est la liste à afficher
    public ExpenseReportAdapter(Context context, List<ExpenseReport> expenseReports) {
        super(context, 0, expenseReports);
    }

    // fonction qui gère la composition d'une cellule de la ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            // on insuffle dans la cellule le layout créé pour la cellule
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_expense_report,parent, false);
        }

        // la note de frais située à X position dans la liste
        final ExpenseReport er = getItem(position);

        // instance du "controlleur" de la cellule
        ListViewExpenseReport viewHolder = new ListViewExpenseReport();
        // on fait le lien avec les TextView du layout de cellule RowExpense
        viewHolder.date = (TextView) convertView.findViewById(R.id.dateExpenseReport);
        viewHolder.city = (TextView) convertView.findViewById(R.id.cityExpenseReport);
        viewHolder.comment = (TextView) convertView.findViewById(R.id.commentExpenseReport);
        viewHolder.amount = (TextView) convertView.findViewById(R.id.expenseTotal);
        viewHolder.deleteER = (Button) convertView.findViewById(R.id.deleteButtonER);
        // si la note de frais est déjà soumise, on enlève le bouton Supprimer
        if (!er.getSubmissionDate().equals("null")) {
            viewHolder.deleteER.setVisibility(View.GONE);
        // sinon, on affiche le bouton Supprimer
        } else {
            viewHolder.deleteER.setVisibility(View.VISIBLE);
            final ListViewExpenseReport finalViewHolder = viewHolder;
            final View finalConvertView = convertView;
            // au clic sur le bouton
            viewHolder.deleteER.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // on ouvre une boite de dialogue, qui demande de confirmer la suppression
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                    builder1.setMessage("Voulez-vous supprimer la note de frais " + er.getCity() + " ?");
                    builder1.setCancelable(true);
                    // bouton Confirmer
                    builder1.setPositiveButton(
                            "Confirmer",
                            new DialogInterface.OnClickListener() {
                                // au clic sur le bouton Confirmer
                                public void onClick(DialogInterface dialog, int id) {
                                    // la boite de dialogue se ferme
                                    dialog.cancel();
                                    // on envoie la requete http qui supprime la note de frais
                                    String myURL = "http://www.gyejacquot-pierre.fr/API/public/delete/er?expenseReportCode=" + String.valueOf(er.getCode());
                                    //String myURL = "http://10.0.2.2/API/public/delete/er?expenseReportCode=" + String.valueOf(er.getCode());

                                    HttpGetRequest getRequest = new HttpGetRequest();
                                    String result = "";
                                    try {
                                        result = getRequest.execute(myURL).get();
                                    } catch (InterruptedException | ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                    // si la requete a été correctement exécutée
                                    if (result.equals("Succes")) {
                                        // on supprime la note de frais dans la ListView
                                        remove(er);
                                        // on affiche un message qui dit Note de frais supprimée
                                        Toast.makeText(getContext(), "Note de frais supprimée", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    // bouton Annuler
                    builder1.setNegativeButton(
                            "Annuler",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // on ferme la boite de dialogue
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });
        }
        convertView.setTag(viewHolder);

        // on remplit la cellule avec les données de la note de frais
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
