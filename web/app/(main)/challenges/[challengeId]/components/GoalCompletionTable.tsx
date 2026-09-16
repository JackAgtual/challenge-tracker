import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import CompletedGoalElement from "./CompletedGoalElement";
import UncompletedGoalElement from "./UncompletedGoalElement";

type GoalCompletionTableProps = {
  challengeId: number;
  challengeInProgress: boolean;
};

export default async function GoalCompletionTable({
  challengeId,
  challengeInProgress,
}: GoalCompletionTableProps) {
  await getValidSession();

  const { data, error } = await client.GET(
    "/challenges/{challengeId}/goals/completions",
    {
      params: { path: { challengeId } },
    }
  );

  if (error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }
  const {
    goalDefinitionResponse: goalDefinitions,
    goalCompletionRowResponses: rows,
  } = data;

  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Date</TableHead>
          {goalDefinitions.map((def) => (
            <TableHead key={def.id}>{def.name}</TableHead>
          ))}
        </TableRow>
      </TableHeader>
      <TableBody>
        {rows.map((row) => (
          <TableRow key={row.date}>
            <TableCell>{row.date}</TableCell>
            {goalDefinitions.map((goal) => {
              const goalCompletionId = row.completionsPerGoal[goal.id];
              return (
                <TableCell key={goal.id}>
                  {goalCompletionId ? (
                    <CompletedGoalElement
                      challengeId={challengeId}
                      goalDefinitionId={goal.id}
                      goalCompletionId={goalCompletionId}
                      challengeInProgress={challengeInProgress}
                    />
                  ) : (
                    <UncompletedGoalElement
                      challengeId={challengeId}
                      goalDefinitionId={goal.id}
                      date={row.date}
                      challengeInProgress={challengeInProgress}
                    />
                  )}
                </TableCell>
              );
            })}
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
