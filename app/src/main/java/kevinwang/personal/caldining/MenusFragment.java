package kevinwang.personal.caldining;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import MenuFragments.Cafe3Fragment;
import MenuFragments.ClarkKerrFragment;
import MenuFragments.CrossroadsFragment;
import MenuFragments.FoothillFragment;

public class MenusFragment extends Fragment {

    public MenusFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentTabHost tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.fragmentContainer);

        tabHost.addTab(tabHost.newTabSpec("Cross roads").setIndicator(
                "Cross roads"), CrossroadsFragment.class, null);

        tabHost.addTab(tabHost.newTabSpec("Cafe 3").setIndicator(
                "Cafe 3"), Cafe3Fragment.class, null);

        tabHost.addTab(tabHost.newTabSpec("Foothill").setIndicator(
                "Foothill"), FoothillFragment.class, null);

        tabHost.addTab(tabHost.newTabSpec("Clark Kerr").setIndicator(
                "Clark Kerr"), ClarkKerrFragment.class, null);

        return tabHost;
    }

}
