package com.asalfo.wiulgi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = ProfileActivityFragment.class.getSimpleName();
    public static final String FEMALE = "female";
    public static final String MALE = "male";

    public static final int NONE = -1;


    @Nullable
    @BindView(R.id.avatarView)
    ImageView mAvatarView;


    @Nullable
    @BindView(R.id.sign_out)
    TextView mSignOut;

    @Nullable
    @BindView(R.id.sign_out_text)
    TextView mSignOutText;

    @Nullable
    @BindView(R.id.profile_username)
    TextView mUsername;

    @Nullable
    @BindView(R.id.user_email_address)
    TextView mUserEmailAddress;

    @Nullable
    @BindView(R.id.input_firtname)
    TextInputEditText mFirstnameText;

    @Nullable
    @BindView(R.id.input_lastname)
    TextInputEditText mLastnameText;

    @Nullable
    @BindView(R.id.input_birth_date)
    TextInputEditText mBirthDateText;

    @Nullable
    @BindView(R.id.gender)
    RadioGroup mGender;


    private OnFragmentInteractionListener mListener;
    private DatePickerDialog birthDatePickerDialog;
    private SimpleDateFormat mDateFormatter;

    private MenuItem mButtonSave;


    public ProfileActivityFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        setDateTimeField();
        return view;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindUser();

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);

        mButtonSave = menu.getItem(0);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {

            case R.id.profile_save:
                updateProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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


    private void bindUser() {

        TextWatcher mTextWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mButtonSave != null) {
                    mButtonSave.setEnabled(true);
                }
            }
        };

        User user = ProfileManager.getInstance().getUser();
        if (user != null) {

            mAvatarView.setImageDrawable(Utils.createTextDrawable(user.getUsername()));
            mUsername.setText(user.getUsername());
            mUserEmailAddress.setText(user.getEmail());
            mFirstnameText.setText(user.getFirstName());
            mFirstnameText.addTextChangedListener(mTextWatcher);
            mLastnameText.setText(user.getLastName());
            mLastnameText.addTextChangedListener(mTextWatcher);
            mBirthDateText.setText(user.getBirthDate());
            mBirthDateText.addTextChangedListener(mTextWatcher);

            String gender = user.getGender();
            if (gender == null) {
                mGender.check(NONE);
            } else if (gender.toLowerCase().equals(FEMALE)) {
                mGender.check(R.id.female);
            } else if (gender.toLowerCase().equals(MALE)) {
                mGender.check(R.id.male);
            }

            mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    mButtonSave.setEnabled(true);
                }
            });

            mSignOut.setText(getString(R.string.sign_out));
            mSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileManager.getInstance().logOut();
                    getActivity().finish();
                }
            });

            mSignOutText.setText(user.getEmail());

            getActivity().invalidateOptionsMenu();
        }
    }

    private void updateProfile() {

        String firstname = mFirstnameText.getText().toString();
        String lastname = mLastnameText.getText().toString();
        String birtdate = mBirthDateText.getText().toString();
        int selectedIndex = mGender.getCheckedRadioButtonId();
        RadioButton gender = (RadioButton) getActivity().findViewById(selectedIndex);

        User user = ProfileManager.getInstance().getUser();
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setBirthDate(birtdate);
        if (gender != null)
            user.setGender(gender.getText().toString());


        mListener.onProfileChanged(user);

    }


    private void setDateTimeField() {
        mBirthDateText.setOnClickListener(this);


        Calendar newCalendar = Calendar.getInstance();
        birthDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mBirthDateText.setText(mDateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onClick(View v) {
        if (v == mBirthDateText) {
            birthDatePickerDialog.show();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onProfileChanged(User user);

    }
}
