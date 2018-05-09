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

    /** L'adapter prend en entrée la liste des dépenses et le layout et génère pour chaque dépense une cellule formatée */

    // expenses est la liste à afficher
    public ExpenseAdapter(Context context, List<Expense> expenses) {
        super(context, 0, expenses);
    }

    // fonction qui gère la composition d'une cellule de la ListView
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            // on insuffle dans la cellule le layout créé pour la cellule
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_expense,parent, false);
        }

        // la dépense située à X position dans la liste
        final Expense e = getItem(position);

        // instance du "controlleur" de la cellule
        ListViewExpense viewHolder = new ListViewExpense();
        // on fait le lien avec les TextView du layout de cellule RowExpense
        viewHolder.dateExpense = (TextView) convertView.findViewById(R.id.dateExpense);
        viewHolder.categoryExpense = (TextView) convertView.findViewById(R.id.labelExpense);
        viewHolder.commentExpense = (TextView) convertView.findViewById(R.id.commentExpense);
        viewHolder.amountExpense = (TextView) convertView.findViewById(R.id.amountExpense);
        viewHolder.deleteE = (Button) convertView.findViewById(R.id.deleteButtonE);
        // si la note de frais est déjà soumise, on enlève le bouton Supprimer
        if (!e.getSubmissionDate().equals("null")) {
            viewHolder.deleteE.setVisibility(View.GONE);
        // sinon, on affiche le bouton Supprimer
        } else {
            viewHolder.deleteE.setVisibility(View.VISIBLE);
            final ListViewExpense finalViewHolder = viewHolder;
            final View finalConvertView = convertView;
            // au clic sur le bouton
            viewHolder.deleteE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // on ouvre une boite de dialogue, qui demande de confirmer la suppression
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                    builder1.setMessage("Voulez-vous supprimer la dépense " + e.getLabel() + " ?");
                    builder1.setCancelable(true);
                    // bouton Confirmer
                    builder1.setPositiveButton(
                            "Confirmer",
                            new DialogInterface.OnClickListener() {
                                /** au clic sur le bouton Confirmer */
                                public void onClick(DialogInterface dialog, int id) {
                                    // la boite de dialogue se ferme
                                    dialog.cancel();
                                    // on envoie la requete http qui supprime la dépense
                                    String myURL = "http://www.gyejacquot-pierre.fr/API/public/delete/expense?idExpense="+String.valueOf(e.getIdExpense())+"&category="+String.valueOf(e.getLabel());
                                    //String myURL = "http://10.0.2.2/smartExpenseApi/API/public/delete/expense?idExpense=" + String.valueOf(e.getIdExpense()) + "&category=" + String.valueOf(e.getLabel());

                                    HttpGetRequest getRequest = new HttpGetRequest();
                                    String result = "";
                                    try {
                                        result = getRequest.execute(myURL).get();
                                    } catch (InterruptedException | ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                    // si la requete a été correctement exécutée
                                    if (result.equals("Succes")) {
                                        // on supprime la dépense dans la ListView
                                        remove(e);
                                        // on affiche un message qui dit Dépense supprimée
                                        Toast.makeText(getContext(), "Dépense supprimée", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    // bouton Annuler
                    builder1.setNegativeButton(
                            "Annuler",
                            new DialogInterface.OnClickListener() {
                                // au clic sur le bouton
                                public void onClick(DialogInterface dialog, int id) {
                                    // on ferme la boite de dialogue
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });


            convertView.setTag(viewHolder);
        }

        // on remplit la cellule avec les données de la dépense
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