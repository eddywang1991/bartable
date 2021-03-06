package com.eddystudio.bartbetter.Adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eddystudio.bartbetter.BR;
import com.eddystudio.bartbetter.DI.Application;

import javax.inject.Inject;

public abstract class BaseRecyclerViewAdapter
    extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ItemHolder> {


  @Inject
  Application application;

  @Override
  public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Application.getAppComponet().inject(this);
    View singleItemView =
        LayoutInflater.from(parent.getContext()).inflate(getlayoutId(), parent, false);
    return new ItemHolder(singleItemView);
  }

  @Override
  public void onBindViewHolder(ItemHolder holder, int position) {
    Object obj = getObjectForPosition(position);
    holder.bind(obj);
  }

  protected abstract Object getObjectForPosition(int position);

  @Override
  public void onViewDetachedFromWindow(ItemHolder holder) {
    super.onViewDetachedFromWindow(holder);
    holder.itemView.clearAnimation();
  }

  @Override
  public int getItemViewType(int position) {
    return getLayoutIdForPosition(position);
  }

  protected abstract int getLayoutIdForPosition(int position);

  protected abstract int getlayoutId();

  public static class ItemHolder extends RecyclerView.ViewHolder {

    private final ViewDataBinding binding;

    public ItemHolder(View itemView) {
      super(itemView);
      binding = DataBindingUtil.bind(itemView);
    }

    public void bind(Object obj) {
      binding.setVariable(BR.obj, obj);
      binding.executePendingBindings();
    }
  }
}
