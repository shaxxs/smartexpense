package com.example.mathi.smartexpense.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mathi on 13/04/2018.
 */

/* rôle de mini controlleur, associé à chaque cellule, et qui va stocker les références vers nos sous vues
(dans notre cas : date, ville et commentaire)
Ce contrôleur va ensuite être stocké en tant que propriété de la vue (plus précisément dans l’attribut tag)
afin de pouvoir garder toujours le même principe de recyclage, une vue n’a qu’un seul ViewHolder, et inversement. */

public class ListViewExpense {
    public TextView dateExpense;
    public TextView categoryExpense;
    public TextView commentExpense;
    public TextView amountExpense;
}
