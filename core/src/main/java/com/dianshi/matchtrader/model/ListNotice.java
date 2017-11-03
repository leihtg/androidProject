package com.dianshi.matchtrader.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ListNotice implements List<NoticeItem> {
    List<NoticeItem> array;

    public ListNotice(){
        array = new ArrayList<>();
    }
    @Override
    public void add(int i, NoticeItem noticeItem) {
        array.add(i,noticeItem);
    }

    @Override
    public boolean add(NoticeItem noticeItem) {
        return array.add(noticeItem);
    }

    @Override
    public boolean addAll(int i, Collection<? extends NoticeItem> collection) {
        return array.addAll(i,collection);
    }

    @Override
    public boolean addAll(Collection<? extends NoticeItem> collection) {
        return array.addAll(collection);
    }

    @Override
    public void clear() {
        array.clear();
    }

    @Override
    public boolean contains(Object o) {
        return array.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return array.containsAll(collection);
    }

    @Override
    public NoticeItem get(int i) {
        return array.get(i);
    }

    @Override
    public int indexOf(Object o) {
        return array.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<NoticeItem> iterator() {
        return array.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return array.lastIndexOf(o);
    }

    @Override
    public ListIterator<NoticeItem> listIterator() {
        return array.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<NoticeItem> listIterator(int i) {
        return array.listIterator(i);
    }

    @Override
    public NoticeItem remove(int i) {
        return array.remove(i);
    }

    @Override
    public boolean remove(Object o) {
        return array.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return array.remove(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return array.retainAll(collection);
    }

    @Override
    public NoticeItem set(int i, NoticeItem noticeItem) {
        return array.set(i,noticeItem);
    }

    @Override
    public int size() {
        return array.size();
    }

    @NonNull
    @Override
    public List<NoticeItem> subList(int i, int i1) {
        return array.subList(i,i1);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return array.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        return array.toArray(ts);
    }
}
