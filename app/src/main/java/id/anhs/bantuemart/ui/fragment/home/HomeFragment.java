package id.anhs.bantuemart.ui.fragment.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.anhs.bantuemart.R;
import id.anhs.bantuemart.ui.fragment.iniBaruHome.IniBaruHomeFragment;
import id.anhs.bantuemart.ui.fragment.product.ProductFragment;
import id.anhs.bantuemart.ui.fragment.uyah.UyahFragment;

public class HomeFragment extends Fragment {

    @BindView(R.id.homeTabLayout) TabLayout tabLayout;
    @BindView(R.id.homeViewPager) ViewPager viewPager;

    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Binding & UnBinder
        unbinder = ButterKnife.bind(this, view);

        // Init ViewPager Adapter
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new IniBaruHomeFragment(), "Home");
        adapter.addFragment(new ProductFragment(), "Product");
        adapter.addFragment(new UyahFragment(), "Uyah");

        // Set Adapter to View Pager
        viewPager.setAdapter(adapter);

        // Set Tab Layout
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class HomeViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        HomeViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
