package com.android.menulisaksarajawa.ui.model;

import com.android.menulisaksarajawa.R;

import java.util.ArrayList;
import java.util.List;

public class Aksara{
    public List<Characters> getAksaraAngka() {
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.satu, "1", 1, 1));
        characters.add(new Characters(R.drawable.dua, "2", 1, 1));
        characters.add(new Characters(R.drawable.tiga, "3", 1, 1));
        characters.add(new Characters(R.drawable.empat, "4", 1, 1));
        characters.add(new Characters(R.drawable.lima, "5", 1, 1));
        characters.add(new Characters(R.drawable.enam, "6", 1, 1));
        characters.add(new Characters(R.drawable.tujuh, "7", 1, 1));
        characters.add(new Characters(R.drawable.delapan, "8", 1, 1));
        characters.add(new Characters(R.drawable.sembilan, "9", 1, 1));
        characters.add(new Characters(R.drawable.nol, "0", 1, 1));
        return characters;
    }

    public List<Characters> getAksarCarakan(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.ha, "ha", 1, 1));
        characters.add(new Characters(R.drawable.na, "na", 1, 1));
        characters.add(new Characters(R.drawable.ca, "ca", 1, 1));
        characters.add(new Characters(R.drawable.ra, "ra", 1, 1));
        characters.add(new Characters(R.drawable.ka, "ka", 1, 1));

        characters.add(new Characters(R.drawable.da, "da", 1, 1));
        characters.add(new Characters(R.drawable.ta, "ta", 1, 1));
        characters.add(new Characters(R.drawable.sa, "sa", 1, 1));
        characters.add(new Characters(R.drawable.wa, "wa", 1, 1));
        characters.add(new Characters(R.drawable.la, "la", 1, 1));

        characters.add(new Characters(R.drawable.pa, "pa", 1, 1));
        characters.add(new Characters(R.drawable.dha, "dha", 1, 1));
        characters.add(new Characters(R.drawable.ja, "ja", 1, 1));
        characters.add(new Characters(R.drawable.ya, "ya", 1, 1));
        characters.add(new Characters(R.drawable.nya, "nya", 1, 1));

        characters.add(new Characters(R.drawable.ma, "ma", 1, 1));
        characters.add(new Characters(R.drawable.ga, "ga", 1, 1));
        characters.add(new Characters(R.drawable.ba, "ba", 1, 1));
        characters.add(new Characters(R.drawable.tha, "tha", 1, 1));
        characters.add(new Characters(R.drawable.nga, "nga", 1, 1));
        return characters;
    }

    public List<Characters> getAksarPasangan(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.h, "h", 1, 1));
        characters.add(new Characters(R.drawable.n, "n", 1, 1));
        characters.add(new Characters(R.drawable.c, "c", 1, 1));
        characters.add(new Characters(R.drawable.r, "r", 1, 1));
        characters.add(new Characters(R.drawable.k, "k", 1, 1));

        characters.add(new Characters(R.drawable.d, "d", 1, 1));
        characters.add(new Characters(R.drawable.t, "t", 1, 1));
        characters.add(new Characters(R.drawable.s, "s", 1, 1));
        characters.add(new Characters(R.drawable.w, "w", 1, 1));
        characters.add(new Characters(R.drawable.l, "l", 1, 1));

        characters.add(new Characters(R.drawable.p, "p", 1, 1));
        characters.add(new Characters(R.drawable.dh, "dh", 1, 1));
        characters.add(new Characters(R.drawable.j, "j", 1, 1));
        characters.add(new Characters(R.drawable.y, "y", 1, 1));
        characters.add(new Characters(R.drawable.ny, "ny", 1, 1));

        characters.add(new Characters(R.drawable.m, "m", 1, 1));
        characters.add(new Characters(R.drawable.g, "g", 1, 1));
        characters.add(new Characters(R.drawable.b, "b", 1, 1));
        characters.add(new Characters(R.drawable.th, "th", 1, 1));
        characters.add(new Characters(R.drawable.ng, "ng", 1, 1));
        return characters;
    }

    public List<Characters> getAksarSwara(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.a, "a", 1, 1));
        characters.add(new Characters(R.drawable.i, "i", 1, 1));
        characters.add(new Characters(R.drawable.u, "u", 1, 1));
        characters.add(new Characters(R.drawable.e, "e", 1, 1));
        characters.add(new Characters(R.drawable.o, "o", 1, 1));
        return characters;
    }
}
