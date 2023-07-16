package com.android.menulisaksarajawa.ui.model;

import com.android.menulisaksarajawa.R;

import java.util.ArrayList;
import java.util.List;

public class Aksara{
    public List<Characters> getAksaraAngka() {
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.nol, R.drawable.anim_nol, "0", 1));
        characters.add(new Characters(R.drawable.satu, R.drawable.anim_satu, "1", 1));
        characters.add(new Characters(R.drawable.dua, R.drawable.anim_dua, "2", 1));
        characters.add(new Characters(R.drawable.tiga, R.drawable.anim_tiga, "3", 1));
        characters.add(new Characters(R.drawable.empat, R.drawable.anim_empat, "4", 1));
        characters.add(new Characters(R.drawable.lima, R.drawable.anim_lima, "5", 1));
        characters.add(new Characters(R.drawable.enam, R.drawable.anim_enam, "6", 1));
        characters.add(new Characters(R.drawable.tujuh, R.drawable.anim_tujuh, "7", 1));
        characters.add(new Characters(R.drawable.delapan, R.drawable.anim_delapan, "8", 1));
        characters.add(new Characters(R.drawable.sembilan, R.drawable.anim_sembilan, "9", 1));
        return characters;
    }

    public List<Characters> getAksarCarakan(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.ha, R.drawable.anim_ha, "ha", 1));
        characters.add(new Characters(R.drawable.na, R.drawable.anim_na, "na", 1));
        characters.add(new Characters(R.drawable.ca, R.drawable.anim_ca, "ca", 1));
        characters.add(new Characters(R.drawable.ra, R.drawable.anim_ra, "ra", 1));
        characters.add(new Characters(R.drawable.ka, R.drawable.anim_ka, "ka", 1));

        characters.add(new Characters(R.drawable.da, R.drawable.anim_da, "da", 1));
        characters.add(new Characters(R.drawable.ta, R.drawable.anim_ta, "ta", 1));
        characters.add(new Characters(R.drawable.sa, R.drawable.anim_sa, "sa", 1));
        characters.add(new Characters(R.drawable.wa, R.drawable.anim_wa, "wa", 1));
        characters.add(new Characters(R.drawable.la, R.drawable.anim_la, "la", 1));

        characters.add(new Characters(R.drawable.pa, R.drawable.anim_pa, "pa", 1));
        characters.add(new Characters(R.drawable.dha, R.drawable.anim_dha, "dha", 1));
        characters.add(new Characters(R.drawable.ja, R.drawable.anim_ja, "ja", 1));
        characters.add(new Characters(R.drawable.ya, R.drawable.anim_ya, "ya", 1));
        characters.add(new Characters(R.drawable.nya, R.drawable.anim_nya, "nya", 1));

        characters.add(new Characters(R.drawable.ma, R.drawable.anim_ma, "ma", 1));
        characters.add(new Characters(R.drawable.ga, R.drawable.anim_ga, "ga", 1));
        characters.add(new Characters(R.drawable.ba, R.drawable.anim_ba, "ba", 1));
        characters.add(new Characters(R.drawable.tha, R.drawable.anim_tha, "tha", 1));
        characters.add(new Characters(R.drawable.nga, R.drawable.anim_nga, "nga", 1));
        return characters;
    }

    public List<Characters> getAksarPasangan(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.h, R.drawable.anim_h, "h", 1));
        characters.add(new Characters(R.drawable.n, R.drawable.anim_n, "n", 1));
        characters.add(new Characters(R.drawable.c, R.drawable.anim_c, "c", 1));
        characters.add(new Characters(R.drawable.r, R.drawable.anim_r, "r", 1));
        characters.add(new Characters(R.drawable.k, R.drawable.anim_k, "k", 1));

        characters.add(new Characters(R.drawable.d, R.drawable.anim_d, "d", 1));
        characters.add(new Characters(R.drawable.t, R.drawable.anim_t, "t", 1));
        characters.add(new Characters(R.drawable.s, R.drawable.anim_s, "s", 1));
        characters.add(new Characters(R.drawable.w, R.drawable.anim_w, "w", 1));
        characters.add(new Characters(R.drawable.l, R.drawable.anim_l, "l", 1));

        characters.add(new Characters(R.drawable.p, R.drawable.anim_p, "p", 1));
        characters.add(new Characters(R.drawable.dh, R.drawable.anim_dh, "dh", 1));
        characters.add(new Characters(R.drawable.j, R.drawable.anim_j, "j", 1));
        characters.add(new Characters(R.drawable.y, R.drawable.anim_y, "y", 1));
        characters.add(new Characters(R.drawable.ny, R.drawable.anim_ny, "ny", 1));

        characters.add(new Characters(R.drawable.m, R.drawable.anim_m, "m", 1));
        characters.add(new Characters(R.drawable.g, R.drawable.anim_g, "g", 1));
        characters.add(new Characters(R.drawable.b, R.drawable.anim_b, "b", 1));
        characters.add(new Characters(R.drawable.th, R.drawable.anim_th, "th", 1));
        characters.add(new Characters(R.drawable.ng, R.drawable.anim_ng, "ng", 1));
        return characters;
    }

    public List<Characters> getAksarSwara(){
        List<Characters> characters = new ArrayList<Characters>();

        characters.add(new Characters(R.drawable.a, R.drawable.anim_a, "a", 1));
        characters.add(new Characters(R.drawable.i, R.drawable.anim_i, "i", 1));
        characters.add(new Characters(R.drawable.u, R.drawable.anim_u, "u", 1));
        characters.add(new Characters(R.drawable.e, R.drawable.anim_e, "e", 1));
        characters.add(new Characters(R.drawable.o, R.drawable.anim_o, "o", 1));
        return characters;
    }
}
