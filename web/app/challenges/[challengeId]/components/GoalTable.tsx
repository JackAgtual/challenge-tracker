import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { components } from "@/types/api";
import CompletedGoalElement from "./CompletedGoalElement";
import UncompletedGoalElement from "./UncompletedGoalElement";

type GoalTableProps = {
  challengeId: number;
  rows: components["schemas"]["GoalTableResponse"]["goalCompletionRowResponses"];
  goalDefinitions: components["schemas"]["GoalTableResponse"]["goalDefinitionResponse"];
};

export default async function GoalTable({
  goalDefinitions,
  rows,
  challengeId,
}: GoalTableProps) {
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
                    />
                  ) : (
                    <UncompletedGoalElement
                      challengeId={challengeId}
                      goalDefinitionId={goal.id}
                      date={row.date}
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
