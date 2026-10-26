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
import { CompletedGoalElement } from "./CompletedGoalElement";
import { UncompletedGoalElement } from "./UncompletedGoalElement";

type Props = {
  challengeId: number;
  canModifyGoalCompletions: boolean;
  curParticipantId: number;
};

export async function GoalCompletionTable({
  challengeId,
  canModifyGoalCompletions,
  curParticipantId,
}: Props) {
  await getValidSession();

  const { data, error } = await client.GET(
    "/challenges/{challengeId}/participants/{participantId}/goals/completions",
    {
      params: { path: { challengeId, participantId: curParticipantId } },
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
            <TableHead key={def.id} className="text-center">
              {def.name}
            </TableHead>
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
                  <div className="flex justify-center">
                    {goalCompletionId ? (
                      <CompletedGoalElement
                        challengeId={challengeId}
                        goalDefinitionId={goal.id}
                        goalCompletionId={goalCompletionId}
                        canModifyGoalCompletions={canModifyGoalCompletions}
                      />
                    ) : (
                      <UncompletedGoalElement
                        challengeId={challengeId}
                        goalDefinitionId={goal.id}
                        date={row.date}
                        canModifyGoalCompletions={canModifyGoalCompletions}
                      />
                    )}
                  </div>
                </TableCell>
              );
            })}
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
