-- 1. Insert data into table
INSERT INTO cd.facilities(
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (9, 'Spa', 20, 30, 100000, 800);

-- 2. Insert calculated data into a table
INSERT INTO cd.facilities(
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (
            (
                SELECT
                    MAX(facid)
                FROM
                    cd.facilities
            ) + 1,
            'Spa',
            20,
            30,
            100000,
            800
    );0);

-- 3. Update some existing data
UPDATE
    cd.facilities
SET
    initialoutlay = 10000
WHERE
        facid = 1;

-- 4. Update a row based on the contents of another row
UPDATE
    cd.facilities
SET
    membercost = (
                     SELECT
                         membercost
                     FROM
                         cd.facilities
                     WHERE
                             facid = 0
                 ) * 1.1,
    guestcost = (
                    SELECT
                        guestcost
                    FROM
                        cd.facilities
                    WHERE
                            facid = 0
                ) * 1.1
WHERE
        facid = 1;

-- 5. Delete all bookings
TRUNCATE TABLE cd.bookings;

-- 6. Delete a member from the cd.members table
DELETE FROM
    cd.members
WHERE
        memid = 37;

-- 7. Control which rows are retrieved - part 2
SELECT
    facid,
    name,
    membercost,
    monthlymaintenance
FROM
    cd.facilities
WHERE
        membercost < monthlymaintenance / 50
  and membercost > 0;

-- 8. Basic string searches
SELECT
    *
FROM
    cd.facilities
WHERE
        NAME LIKE '%Tennis%';

-- 9. Matching against multiple possible values
SELECT
    *
FROM
    cd.facilities
WHERE
        facid IN (1, 5);

-- 10. Working with dates
SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
        joindate > '2012-09-01 00:00:00';

-- 11. Combining results from multiple queries
SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name AS surname
FROM
    cd.facilities;

-- 12. Retrieve the start times of members' bookings
SELECT
    cd.bookings.starttime
FROM
    cd.bookings
        INNER JOIN cd.members ON cd.bookings.memid = cd.members.memid
WHERE
        cd.members.firstname = 'David'
  AND cd.members.surname = 'Farrell';

-- 13. Work out the start times of bookings for tennis courts
SELECT
    cd.bookings.starttime as start,
    cd.facilities.name
FROM
    cd.bookings
        JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid
WHERE
        cd.bookings.starttime > '2012-09-21 00:00:00'
  AND cd.bookings.starttime < '2012-09-21 23:59:59'
  AND cd.facilities.name LIKE 'Tennis Court%'
ORDER BY
    starttime ASC;

-- 14. Produce a list of all members, along with their recommender
SELECT
    mem.firstname AS memfname,
    mem.surname AS memsname,
    rec.firstname AS refname,
    rec.surname AS recsname
FROM
    cd.members AS mem
        LEFT JOIN cd.members AS rec ON mem.recommendedby = rec.memid
ORDER BY
    (mem.surname, mem.firstname);

-- 15. Produce a list of all members who have recommended another member
SELECT
    DISTINCT rec.firstname AS firstname,
             rec.surname as surname
FROM
    cd.members
        INNER JOIN cd.members AS rec ON rec.memid = cd.members.recommendedby
ORDER BY
    surname,
    firstname;

-- 16. Produce a list of all members, along with their recommender, using no joins.
select
    distinct mem.firstname || ' ' || mem.surname as member,
             (
                 select
                         rec.firstname || ' ' || rec.surname as recommender
                 from
                     cd.members rec
                 where
                         rec.memid = mem.recommendedby
             )
from
    cd.members mem
order by
    member;

-- 17. Count the number of recommendations each member makes.
SELECT
    recommendedby,
    count(*) as count
FROM
    cd.members
WHERE
    recommendedby is not null
GROUP BY
    recommendedby
ORDER BY
    recommendedby;

-- 18. List the total slots booked per facility
SELECT
    facid,
    sum(slots) AS "Total Slots"
FROM
    cd.bookings
GROUP BY
    facid
ORDER BY
    facid;

-- 19. List the total slots booked per facility in a given month
SELECT
    facid,
    sum(slots) AS "Total Slots"
FROM
    cd.bookings
WHERE
        starttime >= '2012-09-01 00:00:00'
  AND starttime < '2012-09-30 23:59:59'
GROUP BY
    facid
ORDER BY
    sum(slots) ASC;

-- 20. List the total slots booked per facility per month
SELECT
    facid,
    EXTRACT(
            MONTH
            FROM
            starttime
        ) as month,
  sum(slots) as "Time Slots"
from
    cd.bookings
WHERE
    EXTRACT(
    YEAR
    FROM
    starttime
    ) = '2012'
GROUP BY
    facid,
    month
ORDER BY
    facid,
    month;

-- 21. Find the count of members who have made at least one booking
SELECT
    COUNT(distinct memid) as count
FROM
    cd.bookings;

-- 22. List each member's first booking after September 1st 2012
SELECT
    cd.members.surname,
    cd.members.firstname,
    cd.members.memid,
    min(cd.bookings.starttime)
FROM
    cd.members
        JOIN cd.bookings ON cd.members.memid = cd.bookings.memid
WHERE
        starttime >= '2012-09-01 00:00:00'
GROUP BY
    cd.members.memid
ORDER BY
    memid;

-- 23. Produce a list of member names, with each row containing the total member count
SELECT
    count(*) over(),
    firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate;

-- 24. Produce a numbered list of members
SELECT
    row_number() over(
    ORDER BY
      joindate
  ),
    firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate;

-- 25. Output the facility id that has the highest number of slots booked, again
SELECT
    facid,
    total
FROM
    (
        SELECT
            facid,
            sum(slots) AS total,
            rank() over (
        ORDER BY
          sum(slots) DESC
      ) rank
        FROM
            cd.bookings
        GROUP BY
            facid
    ) AS ranked
WHERE
        rank = 1

-- 26. Format the names of members
SELECT
        surname || ', ' || firstname AS name
FROM
    cd.members;

-- 27. Find telephone numbers with parentheses
SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone ~ '[()]';

-- 28. Count the number of members whose surname starts with each letter of the alphabet
SELECT
    SUBSTRING(surname, 1, 1) AS letter,
    COUNT(*)
FROM
    cd.members
GROUP BY
    letter
ORDER BY
    letter;

