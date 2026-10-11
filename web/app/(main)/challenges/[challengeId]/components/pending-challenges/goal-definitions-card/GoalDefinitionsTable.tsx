import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { GoalDefinitionsProps } from ".";
import GoalDefinitionActionMenu from "./GoalDefinitionActionMenu";

export default function GoalDefinitionsTable({
  challengeId,
  goalDefinitions,
}: GoalDefinitionsProps) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Goal</TableHead>
          <TableHead className="text-right">Action</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {goalDefinitions.map((goal) => (
          <TableRow key={goal.id}>
            <TableCell>{goal.name}</TableCell>
            <TableCell className="flex justify-end">
              <GoalDefinitionActionMenu
                challengeId={challengeId}
                goalDefinitionId={goal.id}
              />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
