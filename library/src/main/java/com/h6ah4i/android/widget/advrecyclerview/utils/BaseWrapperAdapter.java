/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.h6ah4i.android.widget.advrecyclerview.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public class BaseWrapperAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private static final String TAG = "BaseWrapperAdapter";
    private static final boolean LOCAL_LOGD = false;

    private RecyclerView.Adapter<VH> mWrappedAdapter;
    private BridgeObserver mBridgeObserver;

    public BaseWrapperAdapter(RecyclerView.Adapter<VH> adapter) {
        mWrappedAdapter = adapter;
        mBridgeObserver = new BridgeObserver(this);
        mWrappedAdapter.registerAdapterDataObserver(mBridgeObserver);
        super.setHasStableIds(mWrappedAdapter.hasStableIds());
    }

    public void release() {
        onRelease();

        if (mWrappedAdapter != null && mBridgeObserver != null) {
            mWrappedAdapter.unregisterAdapterDataObserver(mBridgeObserver);
        }

        mWrappedAdapter = null;
        mBridgeObserver = null;
    }

    protected void onRelease() {
        // override this method if needed
    }

    public RecyclerView.Adapter<VH> getWrappedAdapter() {
        return mWrappedAdapter;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mWrappedAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mWrappedAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        mWrappedAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(VH holder) {
        mWrappedAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(VH holder) {
        mWrappedAdapter.onViewRecycled(holder);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        mWrappedAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return mWrappedAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        mWrappedAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mWrappedAdapter.getItemCount();
    }

    @Override
    public long getItemId(int position) {
        return mWrappedAdapter.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mWrappedAdapter.getItemViewType(position);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    protected void onWrappedAdapterChanged() {
        if (LOCAL_LOGD) {
            Log.d(TAG, "onWrappedAdapterChanged");
        }
    }

    protected void onWrappedAdapterItemRangeChanged(int positionStart, int itemCount) {
        if (LOCAL_LOGD) {
            Log.d(TAG, "onWrappedAdapterItemRangeChanged(positionStart = " + positionStart + ", itemCount = " + itemCount + ")");
        }
    }

    protected void onWrappedAdapterItemRangeInserted(int positionStart, int itemCount) {
        if (LOCAL_LOGD) {
            Log.d(TAG, "onWrappedAdapterItemRangeInserted(positionStart = " + positionStart + ", itemCount = " + itemCount + ")");
        }
    }

    protected void onWrappedAdapterItemRangeRemoved(int positionStart, int itemCount) {
        if (LOCAL_LOGD) {
            Log.d(TAG, "onWrappedAdapterItemRangeRemoved(positionStart = " + positionStart + ", itemCount = " + itemCount + ")");
        }
    }

    protected void onWrappedAdapterRangeMoved(int fromPosition, int toPosition, int itemCount) {
        if (LOCAL_LOGD) {
            Log.d(TAG, "onWrappedAdapterRangeMoved(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ", itemCount = " + itemCount + ")");
        }

        if (itemCount != 1) {
            throw new IllegalStateException("itemCount should be always 1  (actual: " + itemCount + ")");
        }
    }

    private static final class BridgeObserver<VH extends RecyclerView.ViewHolder> extends RecyclerView.AdapterDataObserver {
        private WeakReference<BaseWrapperAdapter<VH>> mRefHolder;

        public BridgeObserver(BaseWrapperAdapter<VH> holder) {
            mRefHolder = new WeakReference<>(holder);
        }

        @Override
        public void onChanged() {
            final BaseWrapperAdapter<VH> holder = mRefHolder.get();
            if (holder != null) {
                holder.onWrappedAdapterChanged();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            final BaseWrapperAdapter<VH> holder = mRefHolder.get();
            if (holder != null) {
                holder.onWrappedAdapterItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            final BaseWrapperAdapter<VH> holder = mRefHolder.get();
            if (holder != null) {
                holder.onWrappedAdapterItemRangeInserted(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            final BaseWrapperAdapter<VH> holder = mRefHolder.get();
            if (holder != null) {
                holder.onWrappedAdapterItemRangeRemoved(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            final BaseWrapperAdapter<VH> holder = mRefHolder.get();

            if (holder != null) {
                holder.onWrappedAdapterRangeMoved(fromPosition, toPosition, itemCount);
            }
        }
    }
}
