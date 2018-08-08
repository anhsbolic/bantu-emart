package id.anhs.bantuemart.ui.fragment.mainHome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.anhs.bantuemart.R;
import id.anhs.bantuemart.ui.fragment.home.HomeFragment;
import id.anhs.bantuemart.ui.fragment.searchOne.SearchOneFragment;

public class MainHomeFragment extends Fragment implements
        BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.mainHomeBottomNav) BottomNavigationView bottomNavigationView;

    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;
    private Fragment currentFragment, lastFragment;
    private boolean isFirstVisit = true;

    public MainHomeFragment() {
        // Required empty public constructor
    }

    public static MainHomeFragment newInstance() {
        MainHomeFragment fragment = new MainHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);

        // Binding & UnBinder
        unbinder = ButterKnife.bind(this, view);

        // display home page (fragment
        displaySelectedPage(R.id.mainHomeBottomNavHome);

        // navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        return view;
    }

    private void displaySelectedPage(int menuItemId) {
        switch (menuItemId) {
            case R.id.mainHomeBottomNavHome:
                currentFragment = new HomeFragment();
                goToFragment();
                break;
            case R.id.mainHomeBottomNavSearch1:
                currentFragment = new SearchOneFragment();
                goToFragment();
                break;
            case R.id.mainHomeBottomNavSearch2:
                currentFragment = new SearchOneFragment();
                goToFragment();
                break;
            case R.id.mainHomeBottomNavSearch3:
                currentFragment = new SearchOneFragment();
                goToFragment();
                break;
            case R.id.mainHomeBottomNavSearch4:
                currentFragment = new SearchOneFragment();
                goToFragment();
                break;
        }
    }

    private void goToFragment() {
        //replacing the fragment
        if(isFirstVisit){
            isFirstVisit = false;
            Objects.requireNonNull(getFragmentManager()).beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.mainHomeFrameContainer, currentFragment,
                            currentFragment.getClass().getSimpleName())
                    .commit();
            lastFragment = currentFragment;
        }else{
            if(!Objects.equals(currentFragment.getClass().getSimpleName(),
                    lastFragment.getClass().getSimpleName())){
                Objects.requireNonNull(getFragmentManager()).beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.mainHomeFrameContainer, currentFragment,
                                currentFragment.getClass().getSimpleName())
                        .addToBackStack(lastFragment.getClass().getSimpleName())
                        .commit();
                lastFragment = currentFragment;
            }
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedPage(item.getItemId());
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
