package com.android.menulisaksarajawa.ui.model;

import com.android.menulisaksarajawa.R;

import java.util.ArrayList;
import java.util.List;

public class Aksara{
    public List<Characters> getAksaraAngka() {
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.nol, R.drawable.anim_nol, "0", R.raw.nol));
        characters.add(new Characters(R.drawable.satu, R.drawable.anim_satu, "1", R.raw.satu));
        characters.add(new Characters(R.drawable.dua, R.drawable.anim_dua, "2", R.raw.dua));
        characters.add(new Characters(R.drawable.tiga, R.drawable.anim_tiga, "3", R.raw.tiga));
        characters.add(new Characters(R.drawable.empat, R.drawable.anim_empat, "4", R.raw.empat));
        characters.add(new Characters(R.drawable.lima, R.drawable.anim_lima, "5", R.raw.lima));
        characters.add(new Characters(R.drawable.enam, R.drawable.anim_enam, "6", R.raw.enam));
        characters.add(new Characters(R.drawable.tujuh, R.drawable.anim_tujuh, "7", R.raw.tujuh));
        characters.add(new Characters(R.drawable.delapan, R.drawable.anim_delapan, "8", R.raw.delapan));
        characters.add(new Characters(R.drawable.sembilan, R.drawable.anim_sembilan, "9", R.raw.sembilan));
        return characters;
    }

    public List<Characters> getAksarCarakan(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.ha, R.drawable.anim_ha, "ha", R.raw.ha));
        characters.add(new Characters(R.drawable.na, R.drawable.anim_na, "na", R.raw.na));
        characters.add(new Characters(R.drawable.ca, R.drawable.anim_ca, "ca", R.raw.ca));
        characters.add(new Characters(R.drawable.ra, R.drawable.anim_ra, "ra", R.raw.ra));
        characters.add(new Characters(R.drawable.ka, R.drawable.anim_ka, "ka", R.raw.ka));

        characters.add(new Characters(R.drawable.da, R.drawable.anim_da, "da", R.raw.da));
        characters.add(new Characters(R.drawable.ta, R.drawable.anim_ta, "ta", R.raw.ta));
        characters.add(new Characters(R.drawable.sa, R.drawable.anim_sa, "sa", R.raw.sa));
        characters.add(new Characters(R.drawable.wa, R.drawable.anim_wa, "wa", R.raw.wa));
        characters.add(new Characters(R.drawable.la, R.drawable.anim_la, "la", R.raw.la));

        characters.add(new Characters(R.drawable.pa, R.drawable.anim_pa, "pa", R.raw.pa));
        characters.add(new Characters(R.drawable.dha, R.drawable.anim_dha, "dha", R.raw.dha));
        characters.add(new Characters(R.drawable.ja, R.drawable.anim_ja, "ja", R.raw.ja));
        characters.add(new Characters(R.drawable.ya, R.drawable.anim_ya, "ya", R.raw.ya));
        characters.add(new Characters(R.drawable.nya, R.drawable.anim_nya, "nya", R.raw.nya));

        characters.add(new Characters(R.drawable.ma, R.drawable.anim_ma, "ma", R.raw.ma));
        characters.add(new Characters(R.drawable.ga, R.drawable.anim_ga, "ga", R.raw.ga));
        characters.add(new Characters(R.drawable.ba, R.drawable.anim_ba, "ba", R.raw.ba));
        characters.add(new Characters(R.drawable.tha, R.drawable.anim_tha, "tha", R.raw.tha));
        characters.add(new Characters(R.drawable.nga, R.drawable.anim_nga, "nga", R.raw.nga));
        return characters;
    }

    public List<Characters> getAksarPasangan(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.h, R.drawable.anim_h, "h", R.raw.a));
        characters.add(new Characters(R.drawable.n, R.drawable.anim_n, "n", R.raw.a));
        characters.add(new Characters(R.drawable.c, R.drawable.anim_c, "c", R.raw.a));
        characters.add(new Characters(R.drawable.r, R.drawable.anim_r, "r", R.raw.a));
        characters.add(new Characters(R.drawable.k, R.drawable.anim_k, "k", R.raw.a));

        characters.add(new Characters(R.drawable.d, R.drawable.anim_d, "d", R.raw.a));
        characters.add(new Characters(R.drawable.t, R.drawable.anim_t, "t", R.raw.a));
        characters.add(new Characters(R.drawable.s, R.drawable.anim_s, "s", R.raw.a));
        characters.add(new Characters(R.drawable.w, R.drawable.anim_w, "w", R.raw.a));
        characters.add(new Characters(R.drawable.l, R.drawable.anim_l, "l", R.raw.a));

        characters.add(new Characters(R.drawable.p, R.drawable.anim_p, "p", R.raw.a));
        characters.add(new Characters(R.drawable.dh, R.drawable.anim_dh, "dh", R.raw.a));
        characters.add(new Characters(R.drawable.j, R.drawable.anim_j, "j", R.raw.a));
        characters.add(new Characters(R.drawable.y, R.drawable.anim_y, "y", R.raw.a));
        characters.add(new Characters(R.drawable.ny, R.drawable.anim_ny, "ny", R.raw.a));

        characters.add(new Characters(R.drawable.m, R.drawable.anim_m, "m", R.raw.a));
        characters.add(new Characters(R.drawable.g, R.drawable.anim_g, "g", R.raw.a));
        characters.add(new Characters(R.drawable.b, R.drawable.anim_b, "b", R.raw.a));
        characters.add(new Characters(R.drawable.th, R.drawable.anim_th, "th", R.raw.a));
        characters.add(new Characters(R.drawable.ng, R.drawable.anim_ng, "ng", R.raw.a));
        return characters;
    }

    public List<Characters> getAksarSwara(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.a, R.drawable.anim_a, "a", R.raw.a));
        characters.add(new Characters(R.drawable.i, R.drawable.anim_i, "i", R.raw.i));
        characters.add(new Characters(R.drawable.u, R.drawable.anim_u, "u", R.raw.u));
        characters.add(new Characters(R.drawable.e, R.drawable.anim_e, "e", R.raw.e));
        characters.add(new Characters(R.drawable.o, R.drawable.anim_o, "o", R.raw.o));
        return characters;
    }

    public List<Characters> getExercise() {
        List<Characters> words = new ArrayList<Characters>();

        words.add(new Characters(null, null, "JAMAN", null));
        words.add(new Characters(null, null, "PUNA", null));
        return words;
    }
}
