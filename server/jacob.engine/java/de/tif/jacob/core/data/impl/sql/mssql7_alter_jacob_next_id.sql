ALTER PROCEDURE jacob_next_id
(
	@in_tablename	varchar(250),
	@in_increment	int,
	@out_nextid 	int OUTPUT
) AS

DECLARE @nextid int
BEGIN 
	SET @out_nextid = -1

	BEGIN TRANSACTION

	UPDATE jacob_ids SET nextid = nextid + @in_increment WHERE tablename = @in_tablename
	
	-- row already exists?
	IF (@@ROWCOUNT = 0)
		BEGIN
			INSERT INTO jacob_ids (tablename, nextid) VALUES (@in_tablename, 1 + @in_increment)
			SET @nextid = 1
		END
	ELSE
		BEGIN
			SELECT @nextid = nextid - @in_increment FROM jacob_ids WHERE tablename = @in_tablename
		END

	-- everything alright?
	IF (@@ERROR = 0)
		SET @out_nextid = @nextid


	COMMIT TRANSACTION
END
