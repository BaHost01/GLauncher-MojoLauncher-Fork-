package net.kdt.pojavlaunch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import git.artdeell.mojo.R;
import net.kdt.pojavlaunch.instances.Instance;
import net.kdt.pojavlaunch.instances.Instances;

public class InstalledModsFragment extends Fragment {
    public static final String TAG = "InstalledModsFragment";

    private RecyclerView mRecyclerView;
    private ModsAdapter mAdapter;
    private File mModsDir;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance instance = Instances.loadSelectedInstance();
        if (instance != null) {
            mModsDir = new File(instance.getGameDirectory(), "mods");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_installed_mods, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.installed_mods_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        loadMods();
    }

    private void loadMods() {
        List<File> mods = new ArrayList<>();
        if (mModsDir != null && mModsDir.exists() && mModsDir.isDirectory()) {
            File[] files = mModsDir.listFiles(f -> f.isFile() && f.getName().endsWith(".jar"));
            if (files != null) {
                mods.addAll(Arrays.asList(files));
            }
        }
        
        mAdapter = new ModsAdapter(mods);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class ModsAdapter extends RecyclerView.Adapter<ModViewHolder> {
        private final List<File> mModFiles;

        ModsAdapter(List<File> modFiles) {
            mModFiles = modFiles;
        }

        @NonNull
        @Override
        public ModViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_installed_mod, parent, false);
            return new ModViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ModViewHolder holder, int position) {
            File modFile = mModFiles.get(position);
            holder.mNameText.setText(modFile.getName());
            holder.mDeleteButton.setOnClickListener(v -> {
                if (modFile.delete()) {
                    mModFiles.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mModFiles.size());
                } else {
                    Toast.makeText(getContext(), "Failed to delete mod", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mModFiles.size();
        }
    }

    private static class ModViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameText;
        final ImageButton mDeleteButton;

        ModViewHolder(View v) {
            super(v);
            mNameText = v.findViewById(R.id.mod_name_textview);
            mDeleteButton = v.findViewById(R.id.mod_delete_button);
        }
    }
}