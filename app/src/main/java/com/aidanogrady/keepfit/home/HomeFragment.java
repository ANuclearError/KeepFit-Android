package com.aidanogrady.keepfit.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.aidanogrady.keepfit.R;
import com.aidanogrady.keepfit.data.model.Update;
import com.google.common.base.Strings;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The HomeFragment is the primary component of the app. Here the usual is able to see a brief
 * overview of their progress. The user is able to select the goal they wish to work on, and can
 * update how much they have walked that day.
 *
 * @author Aidan O'Grady
 * @since 0.5
 */
public class HomeFragment extends Fragment implements HomeContract.View {
    /**
     * The presenter for the home fragment.
     */
    private HomeContract.Presenter mPresenter;

    /**
     * The card view of the overview of the day.
     */
    private CardView mHomeCardView;

    /**
     * The adapter for displaying the day's updates.
     */
    private UpdatesAdapter mAdapter;

    /**
     * The recycler view for the adapter.
     */
    private RecyclerView mUpdatesView;

    /**
     * Returns a new home fragment instance.
     *
     * @return new home fragment
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new UpdatesAdapter(new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mHomeCardView = (CardView) root.findViewById(R.id.home_card);

        mUpdatesView = (RecyclerView) root.findViewById(R.id.updates_recycler_view);
        mUpdatesView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mUpdatesView.setLayoutManager(layoutManager);

        Button button = (Button) root.findViewById(R.id.set_goal);
        button.setOnClickListener(view -> mPresenter.setGoal());

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.add_steps);
        fab.setOnClickListener(view -> mPresenter.addSteps());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadCurrent();
        mPresenter.loadProgress();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mHomeCardView != null) {
            mPresenter.loadCurrent();
            mPresenter.loadProgress();
        }
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setCurrentDate(long days) {
        TextView dateTextView = (TextView) mHomeCardView.findViewById(R.id.date);
        LocalDate localDate = LocalDate.ofEpochDay(days);
        dateTextView.setText(localDate.toString());
    }

    @Override
    public void setCurrentGoal(String goalName) {
        TextView goalTextView = (TextView) mHomeCardView.findViewById(R.id.current_goal);
        goalTextView.setText(goalName);
    }

    @Override
    public void setCurrentProgress(double currentSteps, double targetSteps, String unitName) {
        TextView progressTextView = (TextView) mHomeCardView.findViewById(R.id.progress);

        if (currentSteps < 0 || targetSteps < 1) {
            progressTextView.setVisibility(View.INVISIBLE);
        } else {
            String currentStr = String.format(Locale.getDefault(), "%.2f", currentSteps);
            String targetStr = String.format(Locale.getDefault(), "%.2f", targetSteps);
            String progress = currentStr + " / " + targetStr + " " + unitName;
            progressTextView.setVisibility(View.VISIBLE);
            progressTextView.setText(progress);
        }
    }

    @Override
    public void setCurrentPercentage(double percentage) {
        TextView percentageTextView = (TextView) mHomeCardView.findViewById(R.id.percentage);
        if (percentage < 0) {
            percentageTextView.setVisibility(View.INVISIBLE);
        } else {
            String percentageString = String.format(Locale.getDefault(), "%.2f %%", percentage);
            percentageTextView.setVisibility(View.VISIBLE);
            percentageTextView.setText(percentageString);
        }
    }

    @Override
    public void showAddSteps(String[] units) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_steps, null);
        Spinner spinner = (Spinner) dialogView.findViewById(R.id.unit_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_steps)
                .setView(dialogView)
                .setPositiveButton(R.string.ok, (dialog, i) -> {
                    EditText stepsEditText = (EditText) dialogView.findViewById(R.id.steps);
                    String stepsStr = stepsEditText.getText().toString();
                    double dist = Strings.isNullOrEmpty(stepsStr) ? 0 : Double.valueOf(stepsStr);
                    String unitName = spinner.getSelectedItem().toString();
                    mPresenter.addSteps(dist, unitName);
                })
                .setNegativeButton(R.string.cancel, (dialog, i) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showNoGoalsMessage() {
        Snackbar.make(getView(), R.string.no_goals_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSetGoal(Pair<List<String>, List<String>> goals) {
        CharSequence[] cs = goals.first.toArray(new CharSequence[goals.first.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.set_goal)
                .setItems(cs, (dialog, i) -> mPresenter.setCurrentGoal(goals.second.get(i)));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showSelectGoalMessage() {
        Snackbar.make(getView(), R.string.no_goal_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showUpdates(List<Update> updates) {
        mAdapter.replaceData(updates);
        mAdapter.notifyDataSetChanged();
    }
}
