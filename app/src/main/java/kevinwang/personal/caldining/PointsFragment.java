package kevinwang.personal.caldining;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

public class PointsFragment extends Fragment {

    SharedPreferences sp;
    private HashMap<String, Integer> projectedPoints;
    private TextView pointsText;

    public PointsFragment() {}

    public static PointsFragment newInstance(HashMap<String, Integer> projectedPoints) {
        PointsFragment pointsFragment = new PointsFragment();

        Bundle args = new Bundle();
        args.putSerializable("projectedPoints", projectedPoints);
        pointsFragment.setArguments(args);

        return pointsFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        projectedPoints = (HashMap<String, Integer>) getArguments().getSerializable("projectedPoints");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_points, container, false);

        pointsText = (TextView) v.findViewById(R.id.points);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String plan = sp.getString("plan", "Standard");
        if (plan.equals("Ultimate")) {
            pointsText.setText("!!INFINITE DINING HALL FOOD!!");
        } else {
            int points = projectedPoints.get(plan);
            pointsText.setText("" + points);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        String plan = sp.getString("plan", "Standard");
        if (plan.equals("Ultimate")) {
            pointsText.setText("!!INFINITE DINING HALL FOOD!!");
        } else {
            int points = projectedPoints.get(plan);
            pointsText.setText("" + points);
        }

    }

}
