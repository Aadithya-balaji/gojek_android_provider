package com.appoets.gojek.provider.utils;

import com.appoets.gojek.provider.R;
import com.appoets.gojek.provider.model.CountryModel;

import java.util.Arrays;
import java.util.List;

public class Country {

    private static final CountryModel[] COUNTRIES = {
            new CountryModel("AD", "Andorra", "+376", R.drawable.flag_ad),
            new CountryModel("AE", "United Arab Emirates", "+971", R.drawable.flag_ae),
            new CountryModel("AF", "Afghanistan", "+93", R.drawable.flag_af),
            new CountryModel("AG", "Antigua and Barbuda", "+1", R.drawable.flag_ag),
            new CountryModel("AI", "Anguilla", "+1", R.drawable.flag_ai),
            new CountryModel("AL", "Albania", "+355", R.drawable.flag_al),
            new CountryModel("AM", "Armenia", "+374", R.drawable.flag_am),
            new CountryModel("AO", "Angola", "+244", R.drawable.flag_ao),
            new CountryModel("AQ", "Antarctica", "+672", R.drawable.flag_aq),
            new CountryModel("AR", "Argentina", "+54", R.drawable.flag_ar),
            new CountryModel("AS", "AmericanSamoa", "+1", R.drawable.flag_as),
            new CountryModel("AT", "Austria", "+43", R.drawable.flag_at),
            new CountryModel("AU", "Australia", "+61", R.drawable.flag_au),
            new CountryModel("AW", "Aruba", "+297", R.drawable.flag_aw),
            new CountryModel("AX", "Åland Islands", "+358", R.drawable.flag_ax),
            new CountryModel("AZ", "Azerbaijan", "+994", R.drawable.flag_az),
            new CountryModel("BA", "Bosnia and Herzegovina", "+387", R.drawable.flag_ba),
            new CountryModel("BB", "Barbados", "+1", R.drawable.flag_bb),
            new CountryModel("BD", "Bangladesh", "+880", R.drawable.flag_bd),
            new CountryModel("BE", "Belgium", "+32", R.drawable.flag_be),
            new CountryModel("BF", "Burkina Faso", "+226", R.drawable.flag_bf),
            new CountryModel("BG", "Bulgaria", "+359", R.drawable.flag_bg),
            new CountryModel("BH", "Bahrain", "+973", R.drawable.flag_bh),
            new CountryModel("BI", "Burundi", "+257", R.drawable.flag_bi),
            new CountryModel("BJ", "Benin", "+229", R.drawable.flag_bj),
            new CountryModel("BL", "Saint Barthélemy", "+590", R.drawable.flag_bl),
            new CountryModel("BM", "Bermuda", "+1", R.drawable.flag_bm),
            new CountryModel("BN", "Brunei Darussalam", "+673", R.drawable.flag_bn),
            new CountryModel("BO", "Bolivia, Plurinational State of", "+591", R.drawable.flag_bo),
            new CountryModel("BQ", "Bonaire", "+599", R.drawable.flag_bq),
            new CountryModel("BR", "Brazil", "+55", R.drawable.flag_br),
            new CountryModel("BS", "Bahamas", "+1", R.drawable.flag_bs),
            new CountryModel("BT", "Bhutan", "+975", R.drawable.flag_bt),
            new CountryModel("BV", "Bouvet Island", "+47", R.drawable.flag_bv),
            new CountryModel("BW", "Botswana", "+267", R.drawable.flag_bw),
            new CountryModel("BY", "Belarus", "+375", R.drawable.flag_by),
            new CountryModel("BZ", "Belize", "+501", R.drawable.flag_bz),
            new CountryModel("CA", "Canada", "+1", R.drawable.flag_ca),
            new CountryModel("CC", "Cocos (Keeling) Islands", "+61", R.drawable.flag_cc),
            new CountryModel("CD", "Congo, The Democratic Republic of the", "+243",
                    R.drawable.flag_cd),
            new CountryModel("CF", "Central African Republic", "+236", R.drawable.flag_cf),
            new CountryModel("CG", "Congo", "+242", R.drawable.flag_cg),
            new CountryModel("CH", "Switzerland", "+41", R.drawable.flag_ch),
            new CountryModel("CI", "Ivory Coast", "+225", R.drawable.flag_ci),
            new CountryModel("CK", "Cook Islands", "+682", R.drawable.flag_ck),
            new CountryModel("CL", "Chile", "+56", R.drawable.flag_cl),
            new CountryModel("CM", "Cameroon", "+237", R.drawable.flag_cm),
            new CountryModel("CN", "China", "+86", R.drawable.flag_cn),
            new CountryModel("CO", "Colombia", "+57", R.drawable.flag_co),
            new CountryModel("CR", "Costa Rica", "+506", R.drawable.flag_cr),
            new CountryModel("CU", "Cuba", "+53", R.drawable.flag_cu),
            new CountryModel("CV", "Cape Verde", "+238", R.drawable.flag_cv),
            new CountryModel("CW", "Curacao", "+599", R.drawable.flag_cw),
            new CountryModel("CX", "Christmas Island", "+61", R.drawable.flag_cx),
            new CountryModel("CY", "Cyprus", "+357", R.drawable.flag_cy),
            new CountryModel("CZ", "Czech Republic", "+420", R.drawable.flag_cz),
            new CountryModel("DE", "Germany", "+49", R.drawable.flag_de),
            new CountryModel("DJ", "Djibouti", "+253", R.drawable.flag_dj),
            new CountryModel("DK", "Denmark", "+45", R.drawable.flag_dk),
            new CountryModel("DM", "Dominica", "+1", R.drawable.flag_dm),
            new CountryModel("DO", "Dominican Republic", "+1", R.drawable.flag_do),
            new CountryModel("DZ", "Algeria", "+213", R.drawable.flag_dz),
            new CountryModel("EC", "Ecuador", "+593", R.drawable.flag_ec),
            new CountryModel("EE", "Estonia", "+372", R.drawable.flag_ee),
            new CountryModel("EG", "Egypt", "+20", R.drawable.flag_eg),
            new CountryModel("EH", "Western Sahara", "+212", R.drawable.flag_eh),
            new CountryModel("ER", "Eritrea", "+291", R.drawable.flag_er),
            new CountryModel("ES", "Spain", "+34", R.drawable.flag_es),
            new CountryModel("ET", "Ethiopia", "+251", R.drawable.flag_et),
            new CountryModel("FI", "Finland", "+358", R.drawable.flag_fi),
            new CountryModel("FJ", "Fiji", "+679", R.drawable.flag_fj),
            new CountryModel("FK", "Falkland Islands (Malvinas)", "+500", R.drawable.flag_fk),
            new CountryModel("FM", "Micronesia, Federated States of", "+691", R.drawable.flag_fm),
            new CountryModel("FO", "Faroe Islands", "+298", R.drawable.flag_fo),
            new CountryModel("FR", "France", "+33", R.drawable.flag_fr),
            new CountryModel("GA", "Gabon", "+241", R.drawable.flag_ga),
            new CountryModel("GB", "United Kingdom", "+44", R.drawable.flag_gb),
            new CountryModel("GD", "Grenada", "+1", R.drawable.flag_gd),
            new CountryModel("GE", "Georgia", "+995", R.drawable.flag_ge),
            new CountryModel("GF", "French Guiana", "+594", R.drawable.flag_gf),
            new CountryModel("GG", "Guernsey", "+44", R.drawable.flag_gg),
            new CountryModel("GH", "Ghana", "+233", R.drawable.flag_gh),
            new CountryModel("GI", "Gibraltar", "+350", R.drawable.flag_gi),
            new CountryModel("GL", "Greenland", "+299", R.drawable.flag_gl),
            new CountryModel("GM", "Gambia", "+220", R.drawable.flag_gm),
            new CountryModel("GN", "Guinea", "+224", R.drawable.flag_gn),
            new CountryModel("GP", "Guadeloupe", "+590", R.drawable.flag_gp),
            new CountryModel("GQ", "Equatorial Guinea", "+240", R.drawable.flag_gq),
            new CountryModel("GR", "Greece", "+30", R.drawable.flag_gr),
            new CountryModel("GS", "South Georgia and the South Sandwich Islands", "+500",
                    R.drawable.flag_gs),
            new CountryModel("GT", "Guatemala", "+502", R.drawable.flag_gt),
            new CountryModel("GU", "Guam", "+1", R.drawable.flag_gu),
            new CountryModel("GW", "Guinea-Bissau", "+245", R.drawable.flag_gw),
            new CountryModel("GY", "Guyana", "+595", R.drawable.flag_gy),
            new CountryModel("HK", "Hong Kong", "+852", R.drawable.flag_hk),
            new CountryModel("HM", "Heard Island and McDonald Islands", "", R.drawable.flag_hm),
            new CountryModel("HN", "Honduras", "+504", R.drawable.flag_hn),
            new CountryModel("HR", "Croatia", "+385", R.drawable.flag_hr),
            new CountryModel("HT", "Haiti", "+509", R.drawable.flag_ht),
            new CountryModel("HU", "Hungary", "+36", R.drawable.flag_hu),
            new CountryModel("ID", "Indonesia", "+62", R.drawable.flag_id),
            new CountryModel("IE", "Ireland", "+353", R.drawable.flag_ie),
            new CountryModel("IL", "Israel", "+972", R.drawable.flag_il),
            new CountryModel("IM", "Isle of Man", "+44", R.drawable.flag_im),
            new CountryModel("IN", "India", "+91", R.drawable.flag_in),
            new CountryModel("IO", "British Indian Ocean Territory", "+246", R.drawable.flag_io),
            new CountryModel("IQ", "Iraq", "+964", R.drawable.flag_iq),
            new CountryModel("IR", "Iran, Islamic Republic of", "+98", R.drawable.flag_ir),
            new CountryModel("IS", "Iceland", "+354", R.drawable.flag_is),
            new CountryModel("IT", "Italy", "+39", R.drawable.flag_it),
            new CountryModel("JE", "Jersey", "+44", R.drawable.flag_je),
            new CountryModel("JM", "Jamaica", "+1", R.drawable.flag_jm),
            new CountryModel("JO", "Jordan", "+962", R.drawable.flag_jo),
            new CountryModel("JP", "Japan", "+81", R.drawable.flag_jp),
            new CountryModel("KE", "Kenya", "+254", R.drawable.flag_ke),
            new CountryModel("KG", "Kyrgyzstan", "+996", R.drawable.flag_kg),
            new CountryModel("KH", "Cambodia", "+855", R.drawable.flag_kh),
            new CountryModel("KI", "Kiribati", "+686", R.drawable.flag_ki),
            new CountryModel("KM", "Comoros", "+269", R.drawable.flag_km),
            new CountryModel("KN", "Saint Kitts and Nevis", "+1", R.drawable.flag_kn),
            new CountryModel("KP", "North Korea", "+850", R.drawable.flag_kp),
            new CountryModel("KR", "South Korea", "+82", R.drawable.flag_kr),
            new CountryModel("KW", "Kuwait", "+965", R.drawable.flag_kw),
            new CountryModel("KY", "Cayman Islands", "+345", R.drawable.flag_ky),
            new CountryModel("KZ", "Kazakhstan", "+7", R.drawable.flag_kz),
            new CountryModel("LA", "Lao People's Democratic Republic", "+856", R.drawable.flag_la),
            new CountryModel("LB", "Lebanon", "+961", R.drawable.flag_lb),
            new CountryModel("LC", "Saint Lucia", "+1", R.drawable.flag_lc),
            new CountryModel("LI", "Liechtenstein", "+423", R.drawable.flag_li),
            new CountryModel("LK", "Sri Lanka", "+94", R.drawable.flag_lk),
            new CountryModel("LR", "Liberia", "+231", R.drawable.flag_lr),
            new CountryModel("LS", "Lesotho", "+266", R.drawable.flag_ls),
            new CountryModel("LT", "Lithuania", "+370", R.drawable.flag_lt),
            new CountryModel("LU", "Luxembourg", "+352", R.drawable.flag_lu),
            new CountryModel("LV", "Latvia", "+371", R.drawable.flag_lv),
            new CountryModel("LY", "Libyan Arab Jamahiriya", "+218", R.drawable.flag_ly),
            new CountryModel("MA", "Morocco", "+212", R.drawable.flag_ma),
            new CountryModel("MC", "Monaco", "+377", R.drawable.flag_mc),
            new CountryModel("MD", "Moldova, Republic of", "+373", R.drawable.flag_md),
            new CountryModel("ME", "Montenegro", "+382", R.drawable.flag_me),
            new CountryModel("MF", "Saint Martin", "+590", R.drawable.flag_mf),
            new CountryModel("MG", "Madagascar", "+261", R.drawable.flag_mg),
            new CountryModel("MH", "Marshall Islands", "+692", R.drawable.flag_mh),
            new CountryModel("MK", "Macedonia, The Former Yugoslav Republic of", "+389",
                    R.drawable.flag_mk),
            new CountryModel("ML", "Mali", "+223", R.drawable.flag_ml),
            new CountryModel("MM", "Myanmar", "+95", R.drawable.flag_mm),
            new CountryModel("MN", "Mongolia", "+976", R.drawable.flag_mn),
            new CountryModel("MO", "Macao", "+853", R.drawable.flag_mo),
            new CountryModel("MP", "Northern Mariana Islands", "+1", R.drawable.flag_mp),
            new CountryModel("MQ", "Martinique", "+596", R.drawable.flag_mq),
            new CountryModel("MR", "Mauritania", "+222", R.drawable.flag_mr),
            new CountryModel("MS", "Montserrat", "+1", R.drawable.flag_ms),
            new CountryModel("MT", "Malta", "+356", R.drawable.flag_mt),
            new CountryModel("MU", "Mauritius", "+230", R.drawable.flag_mu),
            new CountryModel("MV", "Maldives", "+960", R.drawable.flag_mv),
            new CountryModel("MW", "Malawi", "+265", R.drawable.flag_mw),
            new CountryModel("MX", "Mexico", "+52", R.drawable.flag_mx),
            new CountryModel("MY", "Malaysia", "+60", R.drawable.flag_my),
            new CountryModel("MZ", "Mozambique", "+258", R.drawable.flag_mz),
            new CountryModel("NA", "Namibia", "+264", R.drawable.flag_na),
            new CountryModel("NC", "New Caledonia", "+687", R.drawable.flag_nc),
            new CountryModel("NE", "Niger", "+227", R.drawable.flag_ne),
            new CountryModel("NF", "Norfolk Island", "+672", R.drawable.flag_nf),
            new CountryModel("NG", "Nigeria", "+234", R.drawable.flag_ng),
            new CountryModel("NI", "Nicaragua", "+505", R.drawable.flag_ni),
            new CountryModel("NL", "Netherlands", "+31", R.drawable.flag_nl),
            new CountryModel("NO", "Norway", "+47", R.drawable.flag_no),
            new CountryModel("NP", "Nepal", "+977", R.drawable.flag_np),
            new CountryModel("NR", "Nauru", "+674", R.drawable.flag_nr),
            new CountryModel("NU", "Niue", "+683", R.drawable.flag_nu),
            new CountryModel("NZ", "New Zealand", "+64", R.drawable.flag_nz),
            new CountryModel("OM", "Oman", "+968", R.drawable.flag_om),
            new CountryModel("PA", "Panama", "+507", R.drawable.flag_pa),
            new CountryModel("PE", "Peru", "+51", R.drawable.flag_pe),
            new CountryModel("PF", "French Polynesia", "+689", R.drawable.flag_pf),
            new CountryModel("PG", "Papua New Guinea", "+675", R.drawable.flag_pg),
            new CountryModel("PH", "Philippines", "+63", R.drawable.flag_ph),
            new CountryModel("PK", "Pakistan", "+92", R.drawable.flag_pk),
            new CountryModel("PL", "Poland", "+48", R.drawable.flag_pl),
            new CountryModel("PM", "Saint Pierre and Miquelon", "+508", R.drawable.flag_pm),
            new CountryModel("PN", "Pitcairn", "+872", R.drawable.flag_pn),
            new CountryModel("PR", "Puerto Rico", "+1", R.drawable.flag_pr),
            new CountryModel("PS", "Palestinian Territory, Occupied", "+970", R.drawable.flag_ps),
            new CountryModel("PT", "Portugal", "+351", R.drawable.flag_pt),
            new CountryModel("PW", "Palau", "+680", R.drawable.flag_pw),
            new CountryModel("PY", "Paraguay", "+595", R.drawable.flag_py),
            new CountryModel("QA", "Qatar", "+974", R.drawable.flag_qa),
            new CountryModel("RE", "Réunion", "+262", R.drawable.flag_re),
            new CountryModel("RO", "Romania", "+40", R.drawable.flag_ro),
            new CountryModel("RS", "Serbia", "+381", R.drawable.flag_rs),
            new CountryModel("RU", "Russia", "+7", R.drawable.flag_ru),
            new CountryModel("RW", "Rwanda", "+250", R.drawable.flag_rw),
            new CountryModel("SA", "Saudi Arabia", "+966", R.drawable.flag_sa),
            new CountryModel("SB", "Solomon Islands", "+677", R.drawable.flag_sb),
            new CountryModel("SC", "Seychelles", "+248", R.drawable.flag_sc),
            new CountryModel("SD", "Sudan", "+249", R.drawable.flag_sd),
            new CountryModel("SE", "Sweden", "+46", R.drawable.flag_se),
            new CountryModel("SG", "Singapore", "+65", R.drawable.flag_sg),
            new CountryModel("SH", "Saint Helena, Ascension and Tristan Da Cunha", "+290",
                    R.drawable.flag_sh),
            new CountryModel("SI", "Slovenia", "+386", R.drawable.flag_si),
            new CountryModel("SJ", "Svalbard and Jan Mayen", "+47", R.drawable.flag_sj),
            new CountryModel("SK", "Slovakia", "+421", R.drawable.flag_sk),
            new CountryModel("SL", "Sierra Leone", "+232", R.drawable.flag_sl),
            new CountryModel("SM", "San Marino", "+378", R.drawable.flag_sm),
            new CountryModel("SN", "Senegal", "+221", R.drawable.flag_sn),
            new CountryModel("SO", "Somalia", "+252", R.drawable.flag_so),
            new CountryModel("SR", "Suriname", "+597", R.drawable.flag_sr),
            new CountryModel("SS", "South Sudan", "+211", R.drawable.flag_ss),
            new CountryModel("ST", "Sao Tome and Principe", "+239", R.drawable.flag_st),
            new CountryModel("SV", "El Salvador", "+503", R.drawable.flag_sv),
            new CountryModel("SX", "Sint Maarten", "+1", R.drawable.flag_sx),
            new CountryModel("SY", "Syrian Arab Republic", "+963", R.drawable.flag_sy),
            new CountryModel("SZ", "Swaziland", "+268", R.drawable.flag_sz),
            new CountryModel("TC", "Turks and Caicos Islands", "+1", R.drawable.flag_tc),
            new CountryModel("TD", "Chad", "+235", R.drawable.flag_td),
            new CountryModel("TF", "French Southern Territories", "+262", R.drawable.flag_tf),
            new CountryModel("TG", "Togo", "+228", R.drawable.flag_tg),
            new CountryModel("TH", "Thailand", "+66", R.drawable.flag_th),
            new CountryModel("TJ", "Tajikistan", "+992", R.drawable.flag_tj),
            new CountryModel("TK", "Tokelau", "+690", R.drawable.flag_tk),
            new CountryModel("TL", "East Timor", "+670", R.drawable.flag_tl),
            new CountryModel("TM", "Turkmenistan", "+993", R.drawable.flag_tm),
            new CountryModel("TN", "Tunisia", "+216", R.drawable.flag_tn),
            new CountryModel("TO", "Tonga", "+676", R.drawable.flag_to),
            new CountryModel("TR", "Turkey", "+90", R.drawable.flag_tr),
            new CountryModel("TT", "Trinidad and Tobago", "+1", R.drawable.flag_tt),
            new CountryModel("TV", "Tuvalu", "+688", R.drawable.flag_tv),
            new CountryModel("TW", "Taiwan", "+886", R.drawable.flag_tw),
            new CountryModel("TZ", "Tanzania, United Republic of", "+255", R.drawable.flag_tz),
            new CountryModel("UA", "Ukraine", "+380", R.drawable.flag_ua),
            new CountryModel("UG", "Uganda", "+256", R.drawable.flag_ug),
            new CountryModel("UM", "U.S. Minor Outlying Islands", "", R.drawable.flag_um),
            new CountryModel("US", "United States", "+1", R.drawable.flag_us),
            new CountryModel("UY", "Uruguay", "+598", R.drawable.flag_uy),
            new CountryModel("UZ", "Uzbekistan", "+998", R.drawable.flag_uz),
            new CountryModel("VA", "Holy See (Vatican City State)", "+379", R.drawable.flag_va),
            new CountryModel("VC", "Saint Vincent and the Grenadines", "+1", R.drawable.flag_vc),
            new CountryModel("VE", "Venezuela, Bolivarian Republic of", "+58", R.drawable.flag_ve),
            new CountryModel("VG", "Virgin Islands, British", "+1", R.drawable.flag_vg),
            new CountryModel("VI", "Virgin Islands, U.S.", "+1", R.drawable.flag_vi),
            new CountryModel("VN", "Viet Nam", "+84", R.drawable.flag_vn),
            new CountryModel("VU", "Vanuatu", "+678", R.drawable.flag_vu),
            new CountryModel("WF", "Wallis and Futuna", "+681", R.drawable.flag_wf),
            new CountryModel("WS", "Samoa", "+685", R.drawable.flag_ws),
            new CountryModel("XK", "Kosovo", "+383", R.drawable.flag_xk),
            new CountryModel("YE", "Yemen", "+967", R.drawable.flag_ye),
            new CountryModel("YT", "Mayotte", "+262", R.drawable.flag_yt),
            new CountryModel("ZA", "South Africa", "+27", R.drawable.flag_za),
            new CountryModel("ZM", "Zambia", "+260", R.drawable.flag_zm),
            new CountryModel("ZW", "Zimbabwe", "+263", R.drawable.flag_zw),
    };
    private static List<CountryModel> allCountriesList;

    public Country() {
        // Primary Constructor
    }

    public static List<CountryModel> getAllCountries() {
        if (allCountriesList == null) {
            allCountriesList = Arrays.asList(COUNTRIES);
        }
        return allCountriesList;
    }
}
