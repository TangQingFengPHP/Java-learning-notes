create or replace view v_user_order_summary as
select
    u.id as user_id,
    u.username,
    count(o.id) as order_count,
    coalesce(sum(o.amount), 0) as total_amount
from users u
left join orders o on o.user_id = u.id
group by u.id, u.username;
