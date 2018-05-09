package com.example.mathi.smartexpense.model;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mathi on 12/04/2018.
 */

/** rôle de mini controlleur, associé à chaque cellule, et qui va recevoir les données de chaque note de frais
Ce contrôleur va ensuite être stocké en tant que propriété de la vue (plus précisément dans l’attribut tag)
afin de pouvoir garder toujours le même principe de recyclage, une vue n’a qu’un seul ViewHolder, et inversement. */

public class ListViewExpenseReport {
    public TextView date;
    public TextView city;
    public TextView comment;
    public TextView amount;
    public Button deleteER;
}
