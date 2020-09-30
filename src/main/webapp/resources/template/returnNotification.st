returnNotification(name,title,isbn,dueDate,libName,address,signature)::=<<Dear, <name>,
we remind you that there is 1 week and 1 day left until the moment
when the book <title> ISBN: <isbn> must be returned.
Due date: <dueDate>.

<if(signature)>
<signature>,
<endif>
<libName>,
<address>.
>>