package cn.dmego.filter;

public class InQueryFilter extends BaseInFilter
{

    public InQueryFilter(FilterVariable left, StringFilter query, boolean notFlag)
    {
        super(left, notFlag);
        filterFragments.add(query);
    }
}