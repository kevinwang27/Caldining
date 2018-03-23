package MenuFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import kevinwang.personal.caldining.R;

public class FoothillFragment extends Fragment {

    private LinearLayout mBreakfastMenu;
    private LinearLayout mBreakfastContainer;
    private LinearLayout mLunchContainer;
    private LinearLayout mDinnerContainer;

    public FoothillFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_foothill, container, false);
        mBreakfastMenu = v.findViewById(R.id.foothill_breakfast_menu);
        mBreakfastContainer = v.findViewById(R.id.foothill_breakfast_container);
        mLunchContainer = v.findViewById(R.id.foothill_lunch_container);
        mDinnerContainer = v.findViewById(R.id.foothill_dinner_container);

        return v;
    }

}
